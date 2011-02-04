package org.json.simple.serialization;

import org.json.simple.parser.BufferedJSONStreamReader;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author kalle
 * @since 2011-02-04 01.12
 */
public class EnumCodec extends Codec<Enum> {

  private Class<Enum> enumClass;

  public EnumCodec(Class<Enum> enumClass) {
    this.enumClass = enumClass;
  }

  @Override
  public void marshal(Enum object, Class definedType, PrintWriter json, String path, int indentation) throws IllegalAccessException, InstantiationException {
    json.print("\"");
    json.print(object.name());
    json.print("\"");
  }

  @Override
  public Enum unmarshal(BufferedJSONStreamReader jsr) throws ParseException, IOException, InstantiationException, IllegalAccessException {
    jsr.next();
    String name = jsr.getStringValue();
    Enum[] constants = enumClass.getEnumConstants();
    for (Enum constant : constants) {
      if (name.equals(constant.name())) {
        return constant;
      }
    }
    throw new InstantiationException("Enum constant named '" + name + "' is not supported.");
  }
}
