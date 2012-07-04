package org.json.simple.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author karl.wettin@kodapan.se
 * @since 2010-sep-27 21:29:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JSON {

  /**
   * @return attribute name this field is mapped to. E.g. JSON attribute "public" which is a reserved name in Java.
   */
  public String attribute(); 

}
