package org.json.simple.parser;

import junit.framework.TestCase;
import org.json.simple.parser.JSONStreamReader.Event;

import java.io.StringReader;

/**
 * @author karl.wettin@kodapan.se
 * @since 2012-07-05 11:41
 */
public class TestJSONStreamReader extends TestCase {


  public void test() throws Exception {

    JSONStreamReader jsr = new JSONStreamReader(new StringReader("{\"a\" :[\"b\",\"c\",\"d\"],\"e\":[[1,true,2.3456],[3,4,false],[5,6,{\"d\":false}]]}"));

    Event event;

//    while ((event = jsr.next()) != null) {
//
//      System.out.println("event = jsr.next();");
//      System.out.println("assertEquals(\""+event.name()+"\", event.name());");
//      if (jsr.getObjectValue() == null) {
//        System.out.println("assertNull(jsr.getObjectValue());");
//      } else {
//        System.out.println("assertEquals(new "+jsr.getObjectValue().getClass().getSimpleName()+"("+jsr.getObjectValue()+"), jsr.getObjectValue());");
//      }
//
//    }


    event = jsr.next();
    assertEquals("START_DOCUMENT", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_OBJECT", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_KEY", event.name());
    assertEquals("a", jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals("b", jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals("c", jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals("d", jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_KEY", event.name());
    assertEquals("e", jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(1l, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(true, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(2.3456d, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(3l, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(4l, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(false, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(5l, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(6l, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("NEXT_VALUE", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_OBJECT", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_KEY", event.name());
    assertEquals("d", jsr.getObjectValue());
    event = jsr.next();
    assertEquals("START_ELEMENT_VALUE", event.name());
    assertEquals(false, jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_OBJECT", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_ARRAY", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_OBJECT", event.name());
    assertNull(jsr.getObjectValue());
    event = jsr.next();
    assertEquals("END_DOCUMENT", event.name());
    assertNull(jsr.getObjectValue());

    assertNull(jsr.next());

  }


}
