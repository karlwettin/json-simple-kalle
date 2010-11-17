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

import java.math.BigDecimal;

/**
 * @author kalle@apache.org
 * @since 2009-jul-03 03:20:06
 */
public class BigDecimalCodec extends PrimitiveCodec<BigDecimal> {

  public String marshal(BigDecimal attributeValue) {
    return String.valueOf(attributeValue);
  }

  public BigDecimal unmarshal(String stringValue) {
    return new BigDecimal(stringValue);
  }
}
