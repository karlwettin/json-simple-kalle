package org.json.simple.serialization;

/**
 * @author karl.wettin@kodapan.se
 * @since 2011-12-05 08:19
 */
public class DefaultPackageClassResolver extends ClassResolver {

  private String defaultPackage;

  public DefaultPackageClassResolver() {
  }

  public DefaultPackageClassResolver(String defaultPackage) {
    this.defaultPackage = defaultPackage;
  }

  @Override
  public Class resolve(String name) throws ClassNotFoundException {
    try {
      return super.resolve(defaultPackage + "." + name);
    } catch (ClassNotFoundException cnfe) {
      return super.resolve(name);
    }
  }

  public String getDefaultPackage() {
    return defaultPackage;
  }

  public void setDefaultPackage(String defaultPackage) {
    this.defaultPackage = defaultPackage;
  }
}
