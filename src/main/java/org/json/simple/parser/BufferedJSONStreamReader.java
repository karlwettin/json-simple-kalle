package org.json.simple.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

/**
 * A subclass of {@link org.json.simple.parser.JSONStreamReader} that allows for moving the cursor backwards.
 * @author karl.wettin@kodapan.se
 * @since 2009-jul-07 04:51:48
 */
public class BufferedJSONStreamReader extends JSONStreamReader {

  public static int DEFAULT_BUFFER_SIZE = 50;

  private final int bufferSize;

  public BufferedJSONStreamReader(Reader json) {
    this(json, DEFAULT_BUFFER_SIZE);
  }

  /**
   * @param json JSON data
   * @param bufferSize Number of historic events and associated values to keep track of, i.e. how many times you can execute {@link #back()} without encountering an exception.
   */
  public BufferedJSONStreamReader(Reader json, int bufferSize) {
    super(json);
    this.bufferSize = bufferSize;
  }

  private LinkedList events = new LinkedList();
  private LinkedList values = new LinkedList();

  private int cursor = 0;

  public String getStringValue() {
    return (String) values.get(cursor);
  }

  public Double getDoubleValue() {
    return (Double) values.get(cursor);
  }

  public Number getNumberValue() {
    return (Number) values.get(cursor);
  }

  public Long getLongValue() {
    return (Long) values.get(cursor);
  }

  public Boolean getBooleanValue() {
    return (Boolean) values.get(cursor);
  }

  public Object getObjectValue() {
    return values.get(cursor);
  }

  public void expectNext(Event event) throws IOException, ParseException {
    Event next = next();
    if (next != event) {
      throw new IOException("Expected " + event + " but got " + next);
    }
  }

  public Event next() throws IOException, ParseException {
    if (cursor > 0) {
      cursor--;
      return (Event)events.get(cursor);
    }
    Event event = super.next();
    Object value = super.getObjectValue();
    events.addFirst(event);
    values.addFirst(value);

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
   * @throws ArrayIndexOutOfBoundsException When seeking beyond buffer size.
   */
  public Event back() {
    return back(1);
  }

  /**
   * Moves the cursor backwards.
   * @param size Number of steps to move the cursor backwards.
   * @return The event at the new position of the cursor.
   * @throws ArrayIndexOutOfBoundsException When seeking beyond buffer size.
   */
  public Event back(int size) {
    cursor += size;
    if (cursor > events.size()) {
      throw new ArrayIndexOutOfBoundsException("Sought beyond buffer! You need to increase the buffer size to avoid this error.");
    }
    return (Event)events.get(cursor);
  }

  public Event getEvent() {
    return (Event)events.get(cursor);
  }

}
