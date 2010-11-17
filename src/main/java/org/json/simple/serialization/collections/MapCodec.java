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
import org.json.simple.parser.ParseException;
import org.json.simple.serialization.Codec;
import org.json.simple.serialization.CodecRegistry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;


/**
 * @author kalle@apache.org
 * @since 2009-jul-03 06:24:52
 */
public class MapCodec extends Codec<Map> {

  private CodecRegistry codecRegistry;

  private Class genericKeyType;
  private Class genericValueType;


  public MapCodec(CodecRegistry codecRegistry, Class genericKeyType, Class genericValueType) {
    this.codecRegistry = codecRegistry;
    this.genericKeyType = genericKeyType;
    this.genericValueType = genericValueType;
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
  public void marshal(Map object, Class definedType, PrintWriter json, String path, int indentation) {
//    json.append("\"java.util.Map<").append(genericKeyType.getName()).append(", ").append(genericValueType.getName()).append("> is unsupported\"");

    json.append("{\n");

    indentation++;

    for (Iterator<Object> it = object.entrySet().iterator(); it.hasNext();) {
      Map.Entry e = (Map.Entry)it.next();

      addIndentation(json, indentation);
      Codec codec = codecRegistry.getCodec(e.getKey().getClass());
      codec.marshal(e.getKey(), genericKeyType, json, path, indentation + 1);
      json.append(": ");
      codec = codecRegistry.getCodec(e.getValue().getClass());
      codec.marshal(e.getValue(), genericValueType, json, path, indentation + 1);
      if (it.hasNext()) {
        json.append(",");
      }
      json.append("\n");

    }

    indentation--;

    addIndentation(json, indentation);
    json.append("}\n");

  }

  /**
   * @param jsr
   * @return
   */
  public Map unmarshal(BufferedJSONStreamReader jsr) throws ParseException, IOException {
    throw new UnsupportedOperationException("Not implemented");
  }
}
