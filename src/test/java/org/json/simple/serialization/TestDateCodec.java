package org.json.simple.serialization;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.StringReader;
import java.util.Date;

/**
 * @author kalle
 * @since 2010-feb-02 19:14:08
 */
public class TestDateCodec extends TestCase {

  private ISO8601CanonicalDateFormat iso8601 = new ISO8601CanonicalDateFormat();

  @Test
  public void test() throws Exception {
    CodecRegistry registry = new CodecRegistry();
    Date now = new Date();
    String json = registry.getCodec(Date.class).marshall(now);
    Date time = registry.getCodec(Date.class).unmarshall(new StringReader("{ \"_class\" : \"java.util.Date\", \"time\" : "+now.getTime()+" }"));
    Date iso = registry.getCodec(Date.class).unmarshall(new StringReader("{ \"_class\" : \"java.util.Date\", \"iso8601\" : \""+iso8601.format(now)+"\" }"));
    Date primitive = registry.getCodec(Date.class).unmarshall(new StringReader("\""+iso8601.format(now)+"\""));

    assertEquals(now, time);
    assertEquals(now, iso);
    assertEquals(now, primitive);

    Foo[] foo = new Foo[4];

    foo[0] = new Foo();
    foo[0].time = now;
    foo[0].value = "bar";
    json = registry.getCodec(Foo.class).marshall(foo[0]);
    foo[1] = registry.getCodec(Foo.class).unmarshall(json);
    foo[2] = registry.getCodec(Foo.class).unmarshall("{ \"time\" : { \"_class\" : \"java.util.Date\", \"iso8601\" : \""+iso8601.format(now)+"\" }, \"value\" : \"bar\"}");
    foo[3] = registry.getCodec(Foo.class).unmarshall("{ \"time\" : { \"_class\" : \"java.util.Date\", \"time\" : "+now.getTime()+" }, \"value\" : \"bar\"}");
    for (int i=1; i<foo.length; i++) {
      assertEquals(foo[0].time, foo[i].time);
      assertEquals(foo[0].value, foo[i].value);
    }

  }

  public static class Foo {
    private Date time;
    private String value;

    public Date getTime() {
      return time;
    }

    public void setTime(Date time) {
      this.time = time;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

}
