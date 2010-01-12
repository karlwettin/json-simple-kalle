package org.json.simple.parser;
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

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

/**
 * A subclass of {@link org.json.simple.parser.JSONStreamReader} that allows for moving the cursor backwards.
 * @author kalle@apache.org
 * @since 2009-jul-07 04:51:48
 */
public class BufferedJSONStreamReader extends JSONStreamReader {

  private boolean debug = false;

  public BufferedJSONStreamReader(Reader json) {
    this(json, 50);
  }

  public BufferedJSONStreamReader(Reader json, int bufferSize) {
    super(json);
    this.bufferSize = bufferSize;
  }

  private LinkedList<Event> events = new LinkedList<Event>();
  private LinkedList<Object> values = new LinkedList<Object>();

  private int bufferSize = 50;
  private int cursor = 0;

  @Override
  public String getStringValue() {
    return (String) values.get(cursor);
  }

  @Override
  public Double getDoubleValue() {
    return (Double) values.get(cursor);
  }

  @Override
  public Number getNumberValue() {
    return (Number) values.get(cursor);
  }

  @Override
  public Long getLongValue() {
    return (Long) values.get(cursor);
  }

  @Override
  public Boolean getBooleanValue() {
    return (Boolean) values.get(cursor);
  }

  @Override
  public Object getObjectValue() {
    return values.get(cursor);
  }

  @Override
  public Event next() throws IOException, ParseException {
    if (cursor > 0) {
      cursor--;
      if (debug) {
        Event event = events.get(cursor);
        Object value = values.get(cursor);
        System.out.println(event + " : " + value + " (retrieved from buffer)");
      }
      return events.get(cursor);
    }
    Event event = super.next();
    Object value = super.getObjectValue();
    events.addFirst(event);
    values.addFirst(value);

    if (debug) {
      System.out.println(event + " : " + value);
    }

    if (events.size() > bufferSize) {
      events.removeLast();
      values.removeLast();
    }

    return event;
  }

  /**
   * Moves the cursor one step back.
   * @see #back(int)
   * @return The event at the new position of the cursor.
   */
  public Event back() {
    return back(1);
  }

  /**
   * Moves the cursor backwards.
   * @param size Number of steps to move the cursor backwards.
   * @return The event at the new position of the cursor.
   */
  public Event back(int size) {
    cursor += size;
    if (cursor > events.size()) {
      throw new ArrayIndexOutOfBoundsException("Seeked beyond buffer.");
    }
    Event event = events.get(cursor);
    Object value = values.get(cursor);

    if (debug) {
      System.out.println(event + " : " + value + " (backed to this position in the buffer)");
    }

    return event;
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }
}
