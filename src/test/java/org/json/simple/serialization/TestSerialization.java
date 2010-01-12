package org.json.simple.serialization;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * @author kalle
 * @since 2009-jul-29 21:37:52
 */
public class TestSerialization extends TestCase {

  public void test() throws Exception {

    CodecRegistry registry = new CodecRegistry();

    Foo foo = new Foo();
    for (int i = 0; i<10; i++) {
      foo.getBars().add(new BarImpl());
      foo.getBarImpls().add(new BarImpl());
    }

    String json = registry.getCodec(Foo.class).marshall(foo);

    System.out.println(json);

    Foo foo2 = registry.getCodec(Foo.class).unmarshall(json);

    System.currentTimeMillis();

  }


  public static class Foo {
    private Date created = new Date();
    private BarImpl barImpl = new BarImpl();
    private Bar bar = new BarImpl();
    private List<Bar> bars = new ArrayList<Bar>();
    private List<BarImpl> barImpls = new ArrayList<BarImpl>();

    public BarImpl getBarImpl() {
      return barImpl;
    }

    public void setBarImpl(BarImpl barImpl) {
      this.barImpl = barImpl;
    }

    public Bar getBar() {
      return bar;
    }

    public void setBar(Bar bar) {
      this.bar = bar;
    }

    public List<BarImpl> getBarImpls() {
      return barImpls;
    }

    public void setBarImpls(List<BarImpl> barImpls) {
      this.barImpls = barImpls;
    }

    public Date getCreated() {
      return created;
    }

    public void setCreated(Date created) {
      this.created = created;
    }

    public List<Bar> getBars() {
      return bars;
    }

    public void setBars(List<Bar> bars) {
      this.bars = bars;
    }
  }

  public static class BarImpl implements Bar {
    private int i;
    private double d;
    private float f;
    private Short s;

    private String text = "hello world\"!";

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public double getD() {
      return d;
    }

    public void setD(double d) {
      this.d = d;
    }

    public float getF() {
      return f;
    }

    public void setF(float f) {
      this.f = f;
    }

    public Short getS() {
      return s;
    }

    public void setS(Short s) {
      this.s = s;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }

}
