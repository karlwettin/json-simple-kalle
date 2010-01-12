package org.json.simple.serialization.collections;
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
import org.json.simple.serialization.BeanCodec;
import org.json.simple.serialization.Codec;
import org.json.simple.serialization.CodecRegistry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author kalle@apache.org
 * @since 2009-jul-03 06:23:04
 */
public abstract class CollectionCodec extends Codec<Collection> {

  private CodecRegistry codecRegistry;
  private Class primitiveGenericType;

  /**
   * @param codecRegistry
   * @param primitiveGenericType ignored if not a primitive list value type
   */
  protected CollectionCodec(CodecRegistry codecRegistry, Class primitiveGenericType) {
    this.codecRegistry = codecRegistry;
    this.primitiveGenericType = primitiveGenericType;
  }

  /**
   * Appends the JSON value for a given object to a StringBuffer.
   * Output should include JSON synxtax such as "", ["",""], { ... }, etc
   *
   * @param object
   * @param definedType
   * @param json
   * @param path
   * @param indentation
   */
  public void marshall(Collection object, Class definedType, PrintWriter json, String path, int indentation) {
    json.append("\n");
    addIndentation(json, indentation);
    json.append("[");
    for (Iterator it = object.iterator(); it.hasNext();) {
      Object item = it.next();
      Codec codec = codecRegistry.getCodec(item.getClass());
      codec.marshall(item, primitiveGenericType, json, path, indentation + 1);
      if (it.hasNext()) {
        json.append(",");
      }
    }
    json.append("\n");
    addIndentation(json, indentation);
    json.append("]");
  }

  public abstract Collection collectionFactory();

  /**
   * @param jsr root object to be unmarshalled
   * @return
   */
  public Collection unmarshall(BufferedJSONStreamReader jsr) throws ParseException, IOException {
    Collection list = collectionFactory();

    JSONStreamReader.Event event = jsr.next();
    if (event != JSONStreamReader.Event.START_ARRAY) {
      throw new RuntimeException("Expected " + JSONStreamReader.Event.START_ARRAY.name() + " but was " + event.name());
    }

    while (event != JSONStreamReader.Event.END_ARRAY) {

      event = jsr.next();
      if (event == JSONStreamReader.Event.START_OBJECT) {

        String field_id = null; // if not null then this is an entity

        event = jsr.next(); // _id?
        if ("_id".equals(jsr.getStringValue())) {
          event = jsr.next(); // value
          field_id = jsr.getStringValue();
          event = jsr.next(); // next
        } else {
          event = jsr.back();
        }

        Class field_class = primitiveGenericType; // this defaults to the defined generic type if nothing else is set.

        event = jsr.next(); // class
        if (classIdentifierFieldName.equals(jsr.getStringValue())) {
          event = jsr.next(); // value
          try {
            field_class = Class.forName(jsr.getStringValue());
          } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
          }
          event = jsr.next();
        } else {
          event = jsr.back();
        }

        // the codecs will seek 1
        if (event == JSONStreamReader.Event.END_OBJECT) {
          event = jsr.back();
        }

        BeanCodec codec = (BeanCodec) codecRegistry.getCodec(field_class);
        list.add(codec.unmarshallBean(jsr));

      } else {
        event = jsr.back();
        list.add(codecRegistry.getCodec(primitiveGenericType).unmarshall(jsr));
      }

      event = jsr.next();
    }

    if (event != JSONStreamReader.Event.END_ARRAY) {
      throw new RuntimeException("Expected " + JSONStreamReader.Event.END_ARRAY.name() + " but was " + event.name());
    }

    return list;
  }

  /**
   * Returns true if the field should not be marshalled to json. A list with size 0 for instance.
   *
   * @param list
   * @return
   */
  @Override
  public boolean isNull(Collection list) {
    return list == null || list.size() == 0;
  }

  public CodecRegistry getCodecRegistry() {
    return codecRegistry;
  }

  public Class getPrimitiveGenericType() {
    return primitiveGenericType;
  }

}

