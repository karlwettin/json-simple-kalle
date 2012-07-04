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
import org.json.simple.parser.JSONStreamReader;
import org.json.simple.serialization.BeanCodec;
import org.json.simple.serialization.ISO8601CanonicalDateFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * @author kalle
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

  @Override
  public Date unmarshal(BufferedJSONStreamReader jsr) throws org.json.simple.parser.ParseException, IOException {

    JSONStreamReader.Event event = jsr.next(); // value or start object

    if (event == JSONStreamReader.Event.START_OBJECT) {

      Date date = null;
      while ((event = jsr.next()) != JSONStreamReader.Event.END_OBJECT) {
        String key = jsr.getStringValue();
        jsr.next(); // value
        if (BeanCodec.classIdentifierFieldName.equals(key)) {
          if (!Date.class.getName().equals(jsr.getStringValue())) {
            throw new IOException("class identifier is supposed to be " + Date.class.getName());
          }
        } else if (key.equalsIgnoreCase("ISO8601")) {
          try {
            date = fmtThreadLocal.get().parse(jsr.getStringValue());
          } catch (ParseException e) {
            throw new IOException("Expected date to be in ISO8601!", e);
          }
        } else if ("time".equals(key)) {
          date = new Date(jsr.getNumberValue().longValue());
        } else {
          throw new IOException("Unknown attribute name: " + key);
        }

        if ((event = jsr.next()) != JSONStreamReader.Event.NEXT_VALUE) {
          break;
        }
      }
      if (date == null) {
        date = new Date();
      }
      return date;

    } else if (event == JSONStreamReader.Event.START_ELEMENT_VALUE) {

      Object object = jsr.getObjectValue();
      if (object instanceof Number) {
        return new Date(((Number) object).longValue());
      } else {
        try {
          return fmtThreadLocal.get().parse(jsr.getStringValue());
        } catch (ParseException e) {
          throw new IOException("Expected date to be in ISO8601!", e);
        }
      }

    } else {
      throw new RuntimeException();
    }
  }

  public String marshal(Date attributeValue) {
    return fmtThreadLocal.get().format(attributeValue);
  }

  public Date unmarshal(String stringValue) {
    try {
      return fmtThreadLocal.get().parse(stringValue);
    } catch (ParseException e) {
      throw new RuntimeException();
    }
  }
}
