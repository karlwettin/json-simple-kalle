package org.json.simple.serialization;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.json.simple.parser.BufferedJSONStreamReader;
import org.json.simple.parser.JSONStreamReader;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


/**
 * @author kalle@apache.org
 * @since 2009-jul-03 08:20:47
 */
public class BeanCodec<T> extends Codec<T> {

  private static final Logger log = LoggerFactory.getLogger(BeanCodec.class);

  private void findSuperClasses(Set<Class> classes, Class current) {
    classes.add(current);
    if (current.getSuperclass() != null && classes.add(current.getSuperclass())) {
      findSuperClasses(classes, current.getSuperclass());
    }
    for (Class _class : current.getInterfaces()) {
      if (classes.add(_class)) {
        findSuperClasses(classes, _class);
      }
    }
  }

  private Class<T> beanClass;
  private CodecRegistry codecRegistry;
  private Map<String, Field> fieldsByName = new LinkedHashMap<String, Field>();

  private T defaultInstance;

  public Object getDefaultValue(Field field) {
    return get(defaultInstance, field);
  }

  public T getDefaultInstance() {
    return defaultInstance;
  }

  public void resolve(CodecRegistry codecRegistry, Class<T> beanClass) throws IllegalAccessException, InstantiationException {

    log.info("Reflecting " + beanClass.getName());

    if (!Modifier.isAbstract(beanClass.getModifiers())
        && !Modifier.isInterface(beanClass.getModifiers())
        && !beanClass.isEnum()) {
      defaultInstance = beanClass.newInstance();
    }

    this.beanClass = beanClass;
    this.codecRegistry = codecRegistry;

    Set<Class> allClasses = new HashSet<Class>();
    findSuperClasses(allClasses, beanClass);

    List<Field> allFields = new ArrayList<Field>();

    for (Class _class : allClasses) {
      if (!codecRegistry.getPrimitiveCodecs().containsKey(_class)) {
        allFields.addAll(Arrays.asList(_class.getDeclaredFields()));
      }
    }

    for (Field field : allFields) {
      log.info("Reflecting " + beanClass.getName() + "#" + field.getName());
      // all static fields are considered transient
      if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
        continue;
      }

      String attribute = field.getName();
      JSON annotation = field.getAnnotation(JSON.class);
      if (annotation != null) {
        if (annotation.attribute() != null) {
          attribute = annotation.attribute();
        }
      }
      fieldsByName.put(attribute, field);


    }

    Set<Class> resolved = new HashSet<Class>();
    if (resolved.add(getBeanClass())) {
      for (Field f : getFieldsByName().values()) {
        if (resolved.add(f.getType())) {
          codecRegistry.getCodec(f.getType());
        }
      }
    }


  }

  /**
   * Returns the "primitive" string representation for a given value.
   * Output often same as String.valueOf(object);
   * This does not include JSON value syntax such as "", ["",""] and { ... }.
   * <p/>
   * Used mostly to marshal primary key values, but all primitives also implement this method.
   *
   * @param object instance to be marshaled
   * @return marshaled valued of parameter object
   * @throws UnsupportedOperationException if the generic type for this codec can not be serialized to a single primitive value.
   */
  @Override
  public String marshal(T object) throws IllegalAccessException, InstantiationException {
    StringWriter sw = new StringWriter(49152);
    PrintWriter pw = new PrintWriter(sw);
    marshal(object, object.getClass(), pw, "", 0);
    pw.flush();
    return sw.toString();
  }


  public void marshal(T object, PrintWriter pw) throws InstantiationException, IllegalAccessException {
    marshal(object, object.getClass(), pw, "", 0);
  }

  /**
   * @see #marshal(Object)
   */
  @Override
  public T unmarshal(String stringValue) throws InstantiationException, IllegalAccessException {
    BufferedJSONStreamReader jsr = new BufferedJSONStreamReader(new StringReader(stringValue));
    try {
      jsr.next(); // START DOCUMENT
      return unmarshal(jsr);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param bean
   * @param definedType
   * @param path
   * @param indentation @return json representation
   */
  public void marshal(T bean, Class definedType, PrintWriter json, String path, int indentation) throws IllegalAccessException, InstantiationException {

    if (log.isDebugEnabled()) {
      log.debug("Marshalling " + path + " " + bean.getClass().getName() + " defined as " + definedType.getName());
    }

    json.append("{\n");

    boolean needsComma = false;

    if (!bean.getClass().equals(definedType)) {
      addIndentation(json, indentation);
      json.append("\"");
      json.append(classIdentifierFieldName);
      json.append("\" : \"");
      json.append(bean.getClass().getName());
      json.append("\",");
    }


    for (Map.Entry<String, Field> fieldEntry : fieldsByName.entrySet()) {
      if (log.isDebugEnabled() && !"".equals(path)) {
        log.debug("Marshalling " + path + "#" + fieldEntry.getValue().getName());
      }
      Object value = get(bean, fieldEntry.getValue());

      Codec codec = codecRegistry.getCodec(fieldEntry.getValue(), value);

      if (!codec.isNull(value)) {
        if (needsComma) {
          json.append(",");
        }

        json.append("\n");
        addIndentation(json, indentation);
        json.append("\"");
        json.append(fieldEntry.getKey());
        json.append("\" : ");

        StringBuilder childPath = new StringBuilder();
        if (path != null) {
          childPath.append(path);
          childPath.append(".");
        }
        childPath.append(fieldEntry.getKey());


        codec.marshal(value, fieldEntry.getValue().getType(), json, childPath.toString(), indentation + 1);

        needsComma = true;
      }
    }

    json.append("}");

  }

  public T unmarshal(BufferedJSONStreamReader jsr) throws ParseException, IOException, InstantiationException, IllegalAccessException {
    jsr.expectNext(JSONStreamReader.Event.START_OBJECT);
    return unmarshalBean(jsr);
  }


  public T unmarshalBean(BufferedJSONStreamReader jsr) throws ParseException, IOException, InstantiationException, IllegalAccessException {

    if (log.isDebugEnabled()) {
      log.debug("Unmarshalling " + beanClass.getName());
    }

    T bean;
    try {
      bean = beanClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(beanClass.getName(), e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    JSONStreamReader.Event event;

    Codec codec = null;
    while ((event = jsr.next()) == JSONStreamReader.Event.START_ELEMENT_KEY) {

      // todo copy to tupplur

      String fieldName = jsr.getStringValue();
      Field field = fieldsByName.get(fieldName);

      if (log.isDebugEnabled()) {
        log.debug("Unmarshalling " + beanClass.getName() + "#" + fieldName);
      }

      if (field == null && classIdentifierFieldName.equals(jsr.getStringValue())) {
        jsr.next();
        if (!beanClass.getName().equals(jsr.getStringValue())) {
          throw new RuntimeException("beanClass and JSON class does not match! " + beanClass.getName() + " vs. " + jsr.getStringValue());
        }
        event = jsr.next();
        if (event != JSONStreamReader.Event.NEXT_VALUE) {
          break;
        } else {
          continue;
        }
      }

      if (field == null) {
        throw new RuntimeException("There is no codec for field '" + fieldName + "' in class " + beanClass.getName());
      }

      event = jsr.next();
      if (event == JSONStreamReader.Event.START_OBJECT) {
        event = jsr.next();
        String aggregateBeanField = jsr.getStringValue();
        if (classIdentifierFieldName.equals(aggregateBeanField)) {
          event = jsr.next();
          try {
            codec = codecRegistry.getCodec(codecRegistry.getClassResolver().resolve(jsr.getStringValue()));
            jsr.back(3);
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
        } else {
          event = jsr.back(2);
          codec = codecRegistry.getCodec(field, null);
          if (log.isDebugEnabled()) {
            log.debug("Aggregate bean did not contain top field named \"" + classIdentifierFieldName + "\".");
          }
        }
      } else {
        event = jsr.back();
        codec = codecRegistry.getCodec(field, null);
      }

      set(bean, field, codec.unmarshal(jsr));


      event = jsr.next();
      if (event != JSONStreamReader.Event.NEXT_VALUE) {
        break;
      }

      // todo end copy to tupplur
    }

    if (event != JSONStreamReader.Event.END_OBJECT) {
      String more;
      if (codec != null) {
        more = " Could it be that last running codec '" + codec.getClass().getName() + "' that doesn't read data from the stream?";
      } else {
        more = " Notice that there was no codec executed!";
      }
      throw new RuntimeException("Expected " + JSONStreamReader.Event.END_OBJECT.name() + " but was " + event.name() + "." + more);
    }


    return bean;
  }

  public Class<T> getBeanClass() {
    return beanClass;
  }

  public CodecRegistry getCodecRegistry() {
    return codecRegistry;
  }

  public Map<String, Field> getFieldsByName() {
    return fieldsByName;
  }

  @Override
  public String toString() {
    return "BeanCodec{" +
        "beanClass=" + beanClass +
        '}';
  }
}