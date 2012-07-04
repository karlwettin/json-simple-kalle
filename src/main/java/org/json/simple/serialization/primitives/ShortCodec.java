package org.json.simple.serialization.primitives;

import org.json.simple.parser.BufferedJSONStreamReader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;

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
/**
 * @author kalle
 * @since 2009-jul-03 03:17:15
 */
public class ShortCodec extends PrimitiveCodec<Short> {

  public void marshal(Short object, Class definedType, PrintWriter json, String path, int indentation) {
    json.append(marshal(object));
  }

  public String marshal(Short attributeValue) {
    return String.valueOf(attributeValue);
  }

  @Override
  public Short unmarshal(BufferedJSONStreamReader jsr) throws ParseException, IOException {
    jsr.next();
    return jsr.getLongValue().shortValue();
  }

  public Short unmarshal(String stringValue) {
    return Short.valueOf(stringValue);
  }
}
