package org.json.simple.serialization;

/**
 * used by beancodec to find class from a string.
 * perhaps you want a default package or something.
 *
 * @author kalle
 * @since 2011-12-05 07:54
 */
public class ClassResolver {

  public Class resolve(String name) throws ClassNotFoundException {
    return Class.forName(name);
  }

}
