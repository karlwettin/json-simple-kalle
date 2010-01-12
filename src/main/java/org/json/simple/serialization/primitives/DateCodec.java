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

import org.json.simple.serialization.ISO8601CanonicalDateFormat;

import java.text.ParseException;
import java.util.Date;

/**
 * @author kalle@apache.org
 * @see org.json.simple.serialization.ISO8601CanonicalDateFormat
 * @since 2009-jul-03 03:20:39
 */
public class DateCodec extends PrimitiveCodec<Date> {

  /**
   * Thread safe DateFormat that can <b>format</b> in the canonical
   * ISO8601 date format, not including the trailing "Z".
   */
  private final static ISO8601CanonicalDateFormat.ThreadLocalDateFormat fmtThreadLocal
      = new ISO8601CanonicalDateFormat.ThreadLocalDateFormat(new ISO8601CanonicalDateFormat());

  public String marshall(Date attributeValue) {
    return fmtThreadLocal.get().format(attributeValue);
  }

  public Date unmarshall(String stringValue) {
    try {
      return fmtThreadLocal.get().parse(stringValue);
    } catch (ParseException e) {
      throw new RuntimeException();
    }
  }
}
