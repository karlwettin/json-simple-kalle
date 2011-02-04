package org.json.simple.serialization;

import junit.framework.TestCase;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author kalle
 * @since 2011-02-03 22.31
 */
public class TestJavascriptCompiler extends TestCase {


  public void test() throws Exception {
    CodecRegistry registry = new CodecRegistry();
    registry.getCodec(A.class);
    registry.getCodec(B.class);
    registry.getCodec(C.class);

    new JavascriptCompiler(registry, new PrintWriter(System.out)).compile();

  }

  public static enum EEE {
    aaa,bbb,ccc
  }

  public static class A {
    private Date date = new Date();
    private Date date2 = new Date(0);
    private int i  =0;
    private String str = "sdf";
    private B b = new B();
    private C c;
    private List<Integer> ii = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
    private List<String> ss = new ArrayList<String>(Arrays.asList("a","b"));
    private EEE eee = EEE.aaa;

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    public Date getDate2() {
      return date2;
    }

    public void setDate2(Date date2) {
      this.date2 = date2;
    }

    public List<String> getSs() {
      return ss;
    }

    public void setSs(List<String> ss) {
      this.ss = ss;
    }

    public EEE getEee() {
      return eee;
    }

    public void setEee(EEE eee) {
      this.eee = eee;
    }

    public List<Integer> getIi() {
      return ii;
    }

    public void setIi(List<Integer> ii) {
      this.ii = ii;
    }

    public int getI() {
      return i;
    }

    public void setI(int i) {
      this.i = i;
    }

    public String getStr() {
      return str;
    }

    public void setStr(String str) {
      this.str = str;
    }

    public B getB() {
      return b;
    }

    public void setB(B b) {
      this.b = b;
    }

    public C getC() {
      return c;
    }

    public void setC(C c) {
      this.c = c;
    }
  }


  public static class B {
    private A a;
    private C c;

    public A getA() {
      return a;
    }

    public void setA(A a) {
      this.a = a;
    }

    public C getC() {
      return c;
    }

    public void setC(C c) {
      this.c = c;
    }
  }

  public static class C {
    private A a;
    private B b;

    public A getA() {
      return a;
    }

    public void setA(A a) {
      this.a = a;
    }

    public B getB() {
      return b;
    }

    public void setB(B b) {
      this.b = b;
    }
  }

}
