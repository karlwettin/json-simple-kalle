This is a fork of json-simple, a Java JSON library.
That project is located at <http://code.google.com/p/json-simple/>.

This version also contains JSONStreamReader, BufferedJSONStreamReader, JSONFormatter and Java bean serialization tools.

Is uses Maven rather than Ant.

JSONStreamReader is rather simple to use, just an extention on the JSON lexer,
and rather similar to XMLStreamReader. Also see BufferedJSONStreamReader.

JSONFormatter does what you think using the stream reader.

So does the BeanCodec. Caveat emptor:

  * Does NOT (yet) support arrays (nor matrices of arrays). Workaround, use some sort of Collection.
  * Does NOT (yet) support Map. Not even sure how this should be mapped to JSON.


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
