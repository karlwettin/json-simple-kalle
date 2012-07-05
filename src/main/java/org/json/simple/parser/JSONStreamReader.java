package org.json.simple.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * A managed JSON lexer
 * with consumer interface similar to {@link javax.xml.stream.XMLStreamReader}.
 * <p/>
 * Based on the code of {@link org.json.simple.parser.JSONParser}
 *
 * @author karl.wettin@kodapan.se
 * @since 2009-jul-05 07:08:51
 */
public class JSONStreamReader {


  /**
   * @param json JSON input reader. This reader will never be closed by this class.
   */
  public JSONStreamReader(Reader json) {
    lexer.yyreset(json);
    status = S_INIT;
    statusStack = new LinkedList();
  }

  public static final int S_INIT = 0;
  public static final int S_IN_FINISHED_VALUE = 1;//string,number,boolean,null,object,array
  public static final int S_IN_OBJECT = 2;
  public static final int S_IN_ARRAY = 3;
  public static final int S_PASSED_PAIR_KEY = 4;
  public static final int S_IN_PAIR_VALUE = 5;
  public static final int S_END = 6;
  public static final int S_IN_ERROR = -1;

  private Yylex lexer = new Yylex((Reader) null);
  private int status = S_INIT;

  private boolean exhausted = false;

  private int peekStatus(LinkedList statusStack) {
    if (statusStack.size() == 0)
      return -1;
    Integer status = (Integer) statusStack.getFirst();
    return status.intValue();
  }

  /**
   * @return The position of the beginning of the current token.
   */
  public int getPosition() {
    return lexer.getPosition();
  }

  // todo get rid of this stack, it consume heap without being used!
  // todo perhaps it can be replaced with a single integer counting up and down?

  private LinkedList statusStack;

  private Yytoken token;

  public Object getObjectValue() {
    return token == null ? null : token.value;
  }

  public String getStringValue() {
    if (token == null || token.value == null) {
      return null;
    }
    return (String) token.value;
  }

  public Double getDoubleValue() {
    if (token == null || token.value == null) {
      return null;
    }
    return (Double) token.value;
  }

  public Number getNumberValue() {
    if (token == null || token.value == null) {
      return null;
    }
    return (Number) token.value;
  }


  public Long getLongValue() {
    if (token == null || token.value == null) {
      return null;
    }
    return (Long) token.value;
  }

  public Boolean getBooleanValue() {
    if (token == null || token.value == null) {
      return null;
    }
    return (Boolean) token.value;
  }

  private boolean nextCalledForTheFirstTime = false;

  /**
   * Moves the cursor one step forward.
   *
   * @return
   * @throws IOException
   * @throws ParseException
   */
  public Event next() throws IOException, ParseException {
    return dnext();
  }

  /** allows for recursive calls when decorated, eg when using BufferedJSONStreamReader */
  private Event dnext() throws IOException, ParseException {

    if (exhausted) {
      token = null; // resets value
      return null;
    }

    if (!nextCalledForTheFirstTime) {
      nextCalledForTheFirstTime = true;
      return Event.START_DOCUMENT;
    }

    token = lexer.yylex();
    if (token == null) {
      exhausted = true;
      return Event.END_DOCUMENT;
    }

    switch (status) {
      case S_INIT:
        switch (token.type) {
          case Yytoken.TYPE_VALUE:
            status = S_IN_FINISHED_VALUE;
            statusStack.addFirst(new Integer(status));
            return Event.START_ELEMENT_VALUE;

          case Yytoken.TYPE_LEFT_BRACE:
            status = S_IN_OBJECT;
            statusStack.addFirst(new Integer(status));
            return Event.START_OBJECT;

          case Yytoken.TYPE_LEFT_SQUARE:
            status = S_IN_ARRAY;
            statusStack.addFirst(new Integer(status));
            return Event.START_ARRAY;

          default:
            status = S_IN_ERROR;
            throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
        }//inner switch

      case S_IN_FINISHED_VALUE:
        if (token.type == Yytoken.TYPE_EOF) {
          return Event.END_ELEMENT_VALUE;
        } else {
          throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
        }

      case S_IN_OBJECT:
        switch (token.type) {
          case Yytoken.TYPE_COMMA:
            return Event.NEXT_VALUE;

          case Yytoken.TYPE_VALUE:
            if (token.value instanceof String) {
              status = S_PASSED_PAIR_KEY;
              statusStack.addFirst(new Integer(status));
              return Event.START_ELEMENT_KEY;
            } else {
              status = S_IN_ERROR;
              throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
            }

          case Yytoken.TYPE_RIGHT_BRACE:
            if (statusStack.size() > 1) {
              statusStack.removeFirst();
              status = peekStatus(statusStack);
            } else {
              status = S_IN_FINISHED_VALUE;
            }
            return Event.END_OBJECT;

          default:
            status = S_IN_ERROR;
            throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
        }//inner switch

      case S_PASSED_PAIR_KEY:
        switch (token.type) {
          case Yytoken.TYPE_COLON:
            return dnext(); // nobody needs to see these.

          case Yytoken.TYPE_VALUE:
            statusStack.removeFirst();
            status = peekStatus(statusStack);
            return Event.START_ELEMENT_VALUE;

          case Yytoken.TYPE_LEFT_SQUARE:
            statusStack.removeFirst();
            status = S_IN_ARRAY;
            statusStack.addFirst(new Integer(status));
            return Event.START_ARRAY;

          case Yytoken.TYPE_LEFT_BRACE:
            statusStack.removeFirst();
            status = S_IN_OBJECT;
            statusStack.addFirst(new Integer(status));
            return Event.START_OBJECT;

          default:
            status = S_IN_ERROR;
            throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
        }

      case S_IN_ARRAY:
        switch (token.type) {
          case Yytoken.TYPE_COMMA:
            return Event.NEXT_VALUE;

          case Yytoken.TYPE_VALUE:
            return Event.START_ELEMENT_VALUE;

          case Yytoken.TYPE_RIGHT_SQUARE:
            if (statusStack.size() > 1) {
              statusStack.removeFirst();
              status = peekStatus(statusStack);
            } else {
              status = S_IN_FINISHED_VALUE;
            }
            return Event.END_ARRAY;

          case Yytoken.TYPE_LEFT_BRACE:
            status = S_IN_OBJECT;
            statusStack.addFirst(new Integer(status));
            return Event.START_OBJECT;

          case Yytoken.TYPE_LEFT_SQUARE:
            status = S_IN_ARRAY;
            statusStack.addFirst(new Integer(status));
            return Event.START_ARRAY;

          default:
            status = S_IN_ERROR;
            throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
        }//inner switch

      case S_IN_ERROR:
        throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
    }//switch

    throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
  }


  /**
   * An instance of this class is the return value of {@link org.json.simple.parser.JSONStreamReader#next()}.
   *
   * <p/>
   *
   * @author karl.wettin@kodapan.se
   * @since 2009-jul-05 14:01:27
   */
  public static final class Event {

    private final String name;
    private final Integer value;

    public static final Event START_DOCUMENT = new Event("START_DOCUMENT", 0);
    public static final Event END_DOCUMENT = new Event("END_DOCUMENT", 1);

    public static final Event START_OBJECT = new Event("START_OBJECT", 2);
    public static final Event END_OBJECT = new Event("END_OBJECT", 3);

    public static final Event START_ELEMENT_KEY = new Event("START_ELEMENT_KEY", 4);

    public static final Event START_ELEMENT_VALUE = new Event("START_ELEMENT_VALUE", 5);
    public static final Event END_ELEMENT_VALUE = new Event("END_ELEMENT_VALUE", 6);

    public static final Event START_ARRAY = new Event("START_ARRAY", 7);
    public static final Event END_ARRAY = new Event("END_ARRAY", 8);

    public static final Event NEXT_VALUE = new Event("NEXT_VALUE", 9);

//    public static final Event KEY_VALUE_SEPARATOR = new Event("KEY_VALUE_SEPARATOR", 10);


    private static Map eventsByName = new HashMap();
    private static Map eventsByValue = new HashMap();

    private static void add(Event event) {
      eventsByName.put(event.name, event);
      eventsByValue.put(event.value, event);
    }

    static {
      add(START_DOCUMENT);
      add(END_DOCUMENT);
      add(START_OBJECT);
      add(END_OBJECT);
      add(START_ELEMENT_KEY);
      add(START_ELEMENT_VALUE);
      add(END_ELEMENT_VALUE);
      add(START_ARRAY);
      add(END_ARRAY);
      add(NEXT_VALUE);
//      add(KEY_VALUE_SEPARATOR);
    }

    public static Event valueOf(int value) {
      return (Event) eventsByValue.get(value);
    }

    public static Event valueOf(String name) {
      return (Event) eventsByName.get(name);
    }

    private Event(String name, Integer value) {
      this.name = name;
      this.value = value;
    }

    public String name() {
      return name;
    }

    public Integer value() {
      return value;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Event event = (Event) o;

      return name.equals(event.name) && value.equals(event.value);
    }

    public int hashCode() {
      int result = name.hashCode();
      result = 31 * result + value.hashCode();
      return result;
    }

    public String toString() {
      return name;
    }
  }

  public Yytoken getToken() {
    return token;
  }
}