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
/**
 * @author karl.wettin@kodapan.se
 * @since 2009-jul-03 05:41:53
 */
public class ByteCodec extends PrimitiveCodec<Byte> {

  public String marshal(Byte attributeValue) {
    return String.format("%02x", attributeValue);
  }

  public Byte unmarshal(String hex) {
    return (byte) (
        (Character.digit(hex.charAt(0), 16) << 4) +
            Character.digit(hex.charAt(1), 16)
    );
  }
}
