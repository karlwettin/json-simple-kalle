package org.json.simple.serialization.primitives;

import org.json.simple.JSONValue;

import java.net.URLEncoder;
import java.net.URLDecoder;

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
 * @author karl.wettin@kodapan.se
 * @since 2009-jul-03 03:15:32
 */
public class StringCodec extends PrimitiveCodec<String> {



  public String marshal(String attributeValue) {
    return escape(attributeValue);
  }

  public String unmarshal(String stringValue) {
    return stringValue;
  }

  public static String escape(String input) {

    // todo iterate forward looking for something to escape before instantiating and copying stuff!

    return JSONValue.escape(input);
  }

}
