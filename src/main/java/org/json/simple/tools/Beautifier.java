package org.json.simple.tools;

import org.json.simple.parser.JSONStreamReader;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.LinkedList;

import org.json.simple.parser.JSONStreamReader.Event;
import org.json.simple.parser.Yytoken;
import org.json.simple.serialization.primitives.StringCodec;

/**
 * Not thread safe.
 *
 * @author kalle
 * @since 2010-okt-08 01:13:40
 */
public class Beautifier {

  public static void main(String[] args) throws Exception {

    Writer out = new PrintWriter(System.out);
    String json = "{\n" +
        "  \"array\" : [\"alice\", \"bob\", \"eve\"],\n" +
        "  \"matrix\" : [\n" +
        "    [123,234,345],\n" +
        "    [456,null,678],\n" +
        "    [890,901,012]\n" +
        "  ],\n" +
        "  \"object\" : {\n" +
        "    \"inner\" : {\n" +
        "      \"array\" : [\"alice\", \"bob\", \"eve\"],\n" +
        "      \"matrix\" : [\n" +
        "        [123,234,345],\n" +
        "        [456,null,678],\n" +
        "        [890,901,012]\n" +
        "      ]\n" +
        "    },\n" +
        "    \"boolean\" : false\n" +
        "  },\n" +
        "  \"array2\" : [{\n" +
        "      \"foo\" : \"bar\"\n" +
        "    }, {\n" +
        "      \"bar\" : \"foo\"\n" +
        "    }\n" +
        "  ]\n" +
        "}";
    new Beautifier().beautify(new StringReader(json), out);
    out.flush();
    
  }

  private int tabulation = 0;
  private String singleTabulationValue = "  ";

  private LinkedList<String> tabulationValueStack = new LinkedList<String>();

  private String increaseTabulation() {
    tabulation++;
    StringBuilder sb = new StringBuilder(singleTabulationValue.length() * tabulation);
    for (int i = 0; i < tabulation; i++) {
      sb.append(singleTabulationValue);
    }
    String tabulation = sb.toString();
    tabulationValueStack.addFirst(tabulation);
    return tabulation;
  }

  private String decreaseTabulation() {
    tabulation--;
    return tabulationValueStack.removeFirst();
  }

  private String newLineValue = "\n";

  private void writeNewLine(Writer output) throws IOException {
    output.write(newLineValue);
  }

  private void writeTabulation(Writer output) throws IOException {
    if (tabulation > 0) {
      output.append(tabulationValueStack.getFirst());
    }
  }

  public String beautify(String input) throws IOException, ParseException {
    StringWriter output = new StringWriter((int)(input.length() * 1.6));
    beautify(new StringReader(input), output);
    return output.toString();
  }

  public void beautify(Reader input, Writer output) throws IOException, ParseException {

    JSONStreamReader jsr = new JSONStreamReader(input);

    jsr.next(); // START_DOCUMENT

    Event event;
    while ((event = jsr.next()) != Event.END_DOCUMENT) {

      if (event == Event.START_OBJECT) {
        output.write("{");
        increaseTabulation();

      } else if (event == Event.END_OBJECT) {
        decreaseTabulation();
        writeNewLine(output);
        writeTabulation(output);
        output.write("}");

      } else if (event == Event.START_ARRAY) {
        output.write("[");
        increaseTabulation();

      } else if (event == Event.END_ARRAY) {
        writeNewLine(output);
        decreaseTabulation();
        writeTabulation(output);
        output.write("]");

      } else if (event == Event.START_ELEMENT_KEY) {
        writeNewLine(output);
        writeTabulation(output);
        output.write('"');
        output.write(jsr.getStringValue());
        output.write("\": ");

      } else if (event == Event.START_ELEMENT_VALUE) {

        switch (jsr.getToken().valueType) {
          case Yytoken.VALUE_TYPE_NULL:
            output.write("null");
            break;

          case Yytoken.VALUE_TYPE_BOOLEAN:
            Boolean value = jsr.getBooleanValue();
            output.write(value ? "true" : "false");
            break;

          case Yytoken.VALUE_TYPE_STRING:
            output.write('"');
            output.write(StringCodec.escape(jsr.getStringValue()));
            output.write('"');
            break;

          case Yytoken.VALUE_TYPE_DOUBLE:
            output.write(String.valueOf(jsr.getDoubleValue()));
            break;

          case Yytoken.VALUE_TYPE_LONG:
            output.write(String.valueOf(jsr.getLongValue()));
            break;
        }

      } else if (event == Event.END_ELEMENT_VALUE) {
        System.currentTimeMillis();

      } else if (event == Event.NEXT_VALUE) {
        output.write(", ");

      } else {
        throw new RuntimeException("Unknown event: " + event.toString());

      }
    }
  }

  public String getNewLineValue() {
    return newLineValue;
  }

  public void setNewLineValue(String newLineValue) {
    this.newLineValue = newLineValue;
  }

  public String getSingleTabulationValue() {
    return singleTabulationValue;
  }

  public void setSingleTabulationValue(String singleTabulationValue) {
    this.singleTabulationValue = singleTabulationValue;
  }
}
