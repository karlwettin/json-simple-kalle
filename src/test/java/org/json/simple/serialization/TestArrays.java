package org.json.simple.serialization;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author kalle
 * @since 2010-okt-06 10:03:57
 */
public class TestArrays extends TestCase {

  @Test
  public void test() throws Exception {

    CodecRegistry registry = new CodecRegistry();

    Foo foo = new Foo();
    foo.setPrimitives(new float[]{1, 2, 3, 4, 5});
    foo.setObjects(new Float[]{6f, 7f, null, 8f, 9f});

    String json = registry.getCodec(Foo.class).marshal(foo);
    System.out.println(json);


  }

  public static class Foo {

    private float[] primitives;
    private Float[] objects;


    public float[] getPrimitives() {
      return primitives;
    }

    public void setPrimitives(float[] primitives) {
      this.primitives = primitives;
    }

    public Float[] getObjects() {
      return objects;
    }

    public void setObjects(Float[] objects) {
      this.objects = objects;
    }
  }


}
