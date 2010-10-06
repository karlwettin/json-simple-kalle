Please visit:
http://code.google.com/p/json-simple/

This is an extention of json-simple. 
It contains JSONStreamReader and Java bean serialization tools.
Is also using Maven rather than Ant.

JSONStreamReader is rather simple to use, just an extention on the JSON lexer,
and rather similar to XMLStreamReader. Also see BufferedJSONStreamReader.
For examples see BeanCodec that is built using above.

Simple bean serialization example:
  
    CodecRegistry registry = new CodecRegistry();
    Foo foo = new Foo();
    foo.setABooleanNamedPublicInJSON(true);
    foo.setText("Bar");
    String json = registry.getCodec(Foo.class).marshall(foo);
    Foo foo2 = registry.getCodec(Foo.class).unmarshall(json);
  }

  public static class Foo {

    private String text;
    @JSON(attribute = "public")
    private boolean aBooleanNamedPublicInJSON;

    // getters and setters...



karl.wettin@gmail.com
