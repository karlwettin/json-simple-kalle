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

import java.text.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Code from Apache Solr.
 * <p/>
 * DateFormat that can <b>format</b> in the canonical ISO8601 date format,
 * not including the trailing "Z".
 *
 * @since 2009-jul-03 03:26:54
 */
public class ISO8601CanonicalDateFormat extends SimpleDateFormat {

  public static TimeZone UTC = TimeZone.getTimeZone("UTC");

  /**
   * TimeZone for DateMath (UTC)
   */
  protected static final TimeZone MATH_TZ = UTC;
  /**
   * Locale for DateMath (Locale.US)
   */
  protected static final Locale MATH_LOCALE = Locale.US;

  /**
   * Fixed TimeZone (UTC) needed for parsing/formating Dates in the
   * canonical representation.
   */
  protected static final TimeZone CANONICAL_TZ = UTC;
  /**
   * Fixed Locale needed for parsing/formating Milliseconds in the
   * canonical representation.
   */
  protected static final Locale CANONICAL_LOCALE = Locale.US;


  protected NumberFormat millisParser = NumberFormat.getIntegerInstance(CANONICAL_LOCALE);

  protected NumberFormat millisFormat = new DecimalFormat(".###", new DecimalFormatSymbols(CANONICAL_LOCALE));

  public ISO8601CanonicalDateFormat() {
    super("yyyy-MM-dd'T'HH:mm:ss", CANONICAL_LOCALE);
    this.setTimeZone(CANONICAL_TZ);
  }

  public Date parse(String i, ParsePosition p) {
    /* delegate to SimpleDateFormat for easy stuff */
    Date d = super.parse(i, p);
    int milliIndex = p.getIndex();
    /* worry aboutthe milliseconds ourselves */
    if (null != d &&
        -1 == p.getErrorIndex() &&
        milliIndex + 1 < i.length() &&
        '.' == i.charAt(milliIndex)) {
      p.setIndex(++milliIndex); // NOTE: ++ to chomp '.'
      Number millis = millisParser.parse(i, p);
      if (-1 == p.getErrorIndex()) {
        int endIndex = p.getIndex();
        d = new Date(d.getTime()
            + (long) (millis.doubleValue() *
            Math.pow(10, (3 - endIndex + milliIndex))));
      }
    }
    return d;
  }

  public StringBuffer format(Date d, StringBuffer toAppendTo,
                             FieldPosition pos) {
    /* delegate to SimpleDateFormat for easy stuff */
    super.format(d, toAppendTo, pos);
    /* worry aboutthe milliseconds ourselves */
    long millis = d.getTime() % 1000l;
    if (0l == millis) {
      return toAppendTo;
    }
    int posBegin = toAppendTo.length();
    toAppendTo.append(millisFormat.format(millis / 1000d));
    if (DateFormat.MILLISECOND_FIELD == pos.getField()) {
      pos.setBeginIndex(posBegin);
      pos.setEndIndex(toAppendTo.length());
    }
    return toAppendTo;
  }

  public Object clone() {
    ISO8601CanonicalDateFormat c
        = (ISO8601CanonicalDateFormat) super.clone();
    c.millisParser = NumberFormat.getIntegerInstance(CANONICAL_LOCALE);
    c.millisFormat = new DecimalFormat(".###",
        new DecimalFormatSymbols(CANONICAL_LOCALE));
    return c;
  }

  public static class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {
    private DateFormat proto;

    public ThreadLocalDateFormat(DateFormat d) {
      super();
      proto = d;
    }

    protected DateFormat initialValue() {
      return (DateFormat) proto.clone();
    }
  }

}
  
