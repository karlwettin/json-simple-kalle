package org.json.simple.serialization;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kalle
 * @since 2010-nov-17 21:39:47
 */
public class TestMap extends TestCase {

  public void test() {

    Foo foo = new Foo();
    foo.getMap().put(1, "1ab");
    foo.getMap().put("2bc", 2);

    Bar bar = new Bar();
    bar.setInteger(3);
    bar.setString("foobar");
    foo.getMap().put(3, bar);

    System.out.println(new CodecRegistry().getCodec(Foo.class).marshal(foo));


  }

  public static class Foo {
    private Map map = new HashMap();

    public Map getMap() {
      return map;
    }

    public void setMap(Map map) {
      this.map = map;
    }
  }

  public static class Bar {

    private String string;
    private Integer integer;

    public String getString() {
      return string;
    }

    public void setString(String string) {
      this.string = string;
    }

    public Integer getInteger() {
      return integer;
    }

    public void setInteger(Integer integer) {
      this.integer = integer;
    }
  }

}
