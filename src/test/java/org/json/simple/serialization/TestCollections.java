package org.json.simple.serialization;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kalle
 * @since 2010-okt-06 14:40:44
 */
public class TestCollections extends TestCase {

  @Ignore
  @Test
  public void test() throws Exception {

    CodecRegistry registry = new CodecRegistry();

    Foo foo = new Foo();

    foo.setList(new ArrayList<Float>());
    foo.getList().add(1f);
    foo.getList().add(2f);
    foo.getList().add(3f);
    foo.getList().add(null);
    foo.getList().add(5f);


    foo.setMatrix(new ArrayList<List<Float>>());
    foo.getMatrix().add(new ArrayList<Float>());
    foo.getMatrix().get(0).add(1f);
    foo.getMatrix().get(0).add(2f);
    foo.getMatrix().get(0).add(3f);

    foo.getMatrix().add(new ArrayList<Float>());
    foo.getMatrix().get(1).add(4f);
    foo.getMatrix().get(1).add(5f);
    foo.getMatrix().get(1).add(6f);
    
    String json = registry.getCodec(Foo.class).marshal(foo);
    System.out.println(json);


  }

  public static class Foo {

    private List<Float> list;
    private List<List<Float>> matrix;

    public List<Float> getList() {
      return list;
    }

    public void setList(List<Float> list) {
      this.list = list;
    }

    public List<List<Float>> getMatrix() {
      return matrix;
    }

    public void setMatrix(List<List<Float>> matrix) {
      this.matrix = matrix;
    }
  }
}
