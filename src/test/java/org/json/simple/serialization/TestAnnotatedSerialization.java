package org.json.simple.serialization;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author kalle
 * @since 2010-okt-06 09:16:45
 */
public class TestAnnotatedSerialization extends TestCase {

  @Test
  public void test() throws Exception {

    CodecRegistry registry = new CodecRegistry();

    Foo foo = new Foo();
    foo.setABooleanNamedPublicInJSON(true);
    foo.setText("Bar");

    String json = registry.getCodec(Foo.class).marshall(foo);

    System.out.println(json);

    Foo foo2 = registry.getCodec(Foo.class).unmarshall(json);

    assertEquals(foo, foo2);

    Foo foo3 = registry.getCodec(Foo.class).unmarshall("{\"text\" : \"Bar\", \"public\" : true}");
    assertEquals("Bar", foo3.getText());
    assertEquals(true, foo3.isABooleanNamedPublicInJSON());
    
    System.currentTimeMillis();


  }


  public static class Foo {

    private String text;
    @JSON(attribute = "public")
    private boolean aBooleanNamedPublicInJSON;


    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public boolean isABooleanNamedPublicInJSON() {
      return aBooleanNamedPublicInJSON;
    }

    public void setABooleanNamedPublicInJSON(boolean aBooleanNamedPublicInJSON) {
      this.aBooleanNamedPublicInJSON = aBooleanNamedPublicInJSON;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Foo foo = (Foo) o;

      if (aBooleanNamedPublicInJSON != foo.aBooleanNamedPublicInJSON) return false;
      if (text != null ? !text.equals(foo.text) : foo.text != null) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = text != null ? text.hashCode() : 0;
      result = 31 * result + (aBooleanNamedPublicInJSON ? 1 : 0);
      return result;
    }
  }

}
