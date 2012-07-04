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
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author karl.wettin@kodapan.se
 * @since 2009-jul-03 14:39:23
 */
public abstract class Codec<T> {

  /** json field name for java class names. "class" is a reserved name in json! */
  public static String classIdentifierFieldName = "_class";


  public T getDefaultInstance() {
    return null;
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
  public String marshal(T object) throws UnsupportedOperationException, IllegalAccessException, InstantiationException {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * @see #marshal(Object)
   */
  public T unmarshal(String stringValue) throws UnsupportedOperationException, InstantiationException, IllegalAccessException {
    throw new UnsupportedOperationException("Not implemented");
  }

  public T unmarshal(Reader reader) throws ParseException, IOException, IllegalAccessException, InstantiationException {
    BufferedJSONStreamReader jsr = new BufferedJSONStreamReader(reader);
    jsr.expectNext(BufferedJSONStreamReader.Event.START_DOCUMENT);
    T t = unmarshal(jsr);
    if (jsr.getEvent() != BufferedJSONStreamReader.Event.END_DOCUMENT) {
      jsr.expectNext(BufferedJSONStreamReader.Event.END_DOCUMENT);
    }
    return t;
  }


  /**
   * Appends the JSON value for a given object to a StringBuffer.
   * Output should include JSON synxtax such as "", ["",""], { ... }, etc
   *
   * @param object          object to be marshaled
   * @param definedType
   * @param json            json string factory
   * @param path            current path in object graph from root
   * @param indentation
   */
  public abstract void marshal(T object, Class definedType, PrintWriter json, String path, int indentation) throws IllegalAccessException, InstantiationException;

  /**
   * @param jsr
   * @return
   */
  public abstract T unmarshal(BufferedJSONStreamReader jsr) throws ParseException, IOException, InstantiationException, IllegalAccessException; // note for self: no Field here, raw object only.

  private Map<Field, Method> getters = new HashMap<Field, Method>();
  private Map<Field, Method> setters = new HashMap<Field, Method>();

  /** @return bean field value via getter */
  public Object get(Object bean, Field field) {
    try {
      return getGetter(field).invoke(bean);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NullPointerException e) {
      throw new RuntimeException(e);
    }
  }


  public void set(Object bean, Field field, Object value) {
    try {
      getSetter(field).invoke(bean, value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private Method getGetter(Field field) {
    Method getter = getters.get(field);
    if (getter == null) {
      Class declaringClass = field.getDeclaringClass();
      StringBuilder getterName = new StringBuilder(field.getName().length() + 3);
      if (field.getType() == boolean.class || Boolean.class.equals(field.getType())) {
        getterName.append("is");
        getterName.append(field.getName());
        getterName.setCharAt(2, Character.toUpperCase(getterName.charAt(2)));
      } else {
        getterName.append("get");
        getterName.append(field.getName());
        getterName.setCharAt(3, Character.toUpperCase(getterName.charAt(3)));
      }

      try {
        getter = declaringClass.getMethod(getterName.toString());
      } catch (NoSuchMethodException e) {
        throw new RuntimeException("Field " + field.getName() + " of class " + declaringClass.getName() + " does not have a getter method named " + getterName);
      }
      getters.put(field, getter);
    }
    return getter;
  }

  private Method getSetter(Field field) {
    Method setter = setters.get(field);
    if (setter == null) {
      Class declaringClass = field.getDeclaringClass();
      StringBuffer name = new StringBuffer(field.getName());
      name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
      String setterName = "set" + name.toString();
      try {
        setter = declaringClass.getMethod(setterName, field.getType());
      } catch (NoSuchMethodException e) {
        throw new RuntimeException("Field " + field.getName() + " of class " + declaringClass.getName() + " does not have a setter method named " + setterName);
      }
      setters.put(field, setter);
    }
    return setter;
  }

  /**
   * Returns true if the field should not be marshaled to json. A list with size 0 for instance.
   *
   * @param value
   * @return
   */
  public boolean isNull(T value) {
    return value == null;
  }

  public void addIndentation(PrintWriter json, int indentations) {
    for (int i = 0; i < indentations; i++) {
      json.append('\t');
    }
  }

}
