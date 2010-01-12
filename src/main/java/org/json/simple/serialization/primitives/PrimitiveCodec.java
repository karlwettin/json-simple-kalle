package org.json.simple.serialization.primitives;
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

import java.io.IOException;
import java.io.PrintWriter;


/**
 * A primitive codec can mashall a single value to a single string and vice verse.
 *
 * @author kalle@apache.org
 * @since 2009-jul-03 14:32:49
 */
public abstract class PrimitiveCodec<T> extends Codec<T> {

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
  public void marshall(T object, Class definedType, PrintWriter json, String path, int indentation) {
    json.append("\"");
    json.append(marshall(object));
    json.append("\"");
  }

  @Override
  public T unmarshall(BufferedJSONStreamReader jsr) throws ParseException, IOException {
    jsr.next();
    return unmarshall(jsr.getStringValue());
  }

  @Override
  public abstract String marshall(T attributeValue);

  @Override
  public abstract T unmarshall(String stringValue);

}
