package org.json.simple.parser;

import org.json.simple.JSONObject;

import java.io.*;
import java.text.DecimalFormat;

/**
 * @author kalle
 * @since 2012-04-06 00:55
 */
public class JSONFormatter {

  public String format(String input) throws Exception {
    StringWriter buffer = new StringWriter((int) (input.length() * 1.1));
    format(new StringReader(input), buffer);
    return buffer.toString();
  }

  public void format(Reader inputReader, Writer output) throws Exception {

    DecimalFormat df = new DecimalFormat("#.0");

    BufferedJSONStreamReader input = new BufferedJSONStreamReader(inputReader);

    int indentation = 0;

    JSONStreamReader.Event event;
    while ((event = input.next()) != null) {

      if (event == JSONStreamReader.Event.START_DOCUMENT) {

      } else if (event == JSONStreamReader.Event.START_OBJECT) {
        JSONStreamReader.Event previous = input.back();
        if (previous == JSONStreamReader.Event.NEXT_VALUE
            || previous == JSONStreamReader.Event.START_ELEMENT_KEY) {
          // spacer already set
        } else {
          addIndentation(output, indentation);
        }
        input.next(); // reset

        output.write('{');
        output.write('\n');
        indentation++;

      } else if (event == JSONStreamReader.Event.END_OBJECT) {
        indentation--;
        addIndentation(output, indentation);
        output.write('}');
        if (input.next() != JSONStreamReader.Event.NEXT_VALUE) {
          output.write('\n');
        }
        input.back(); // reset

      } else if (event == JSONStreamReader.Event.START_ARRAY) {
        JSONStreamReader.Event previous = input.back();
        if (previous == JSONStreamReader.Event.NEXT_VALUE
            || previous == JSONStreamReader.Event.START_ELEMENT_KEY) {
          // spacer already set
        } else {
          addIndentation(output, indentation);
        }
        input.next(); // reset

        output.write('[');
        output.write('\n');
        indentation++;

      } else if (event == JSONStreamReader.Event.END_ARRAY) {
        indentation--;
        addIndentation(output, indentation);
        output.write(']');
        if (input.next() != JSONStreamReader.Event.NEXT_VALUE) {
          output.write('\n');
        }
        input.back(); // reset


      } else if (event == JSONStreamReader.Event.NEXT_VALUE) {
        JSONStreamReader.Event next = input.next();

        if (next == JSONStreamReader.Event.END_ARRAY
            || next == JSONStreamReader.Event.END_OBJECT) {
          // no need for this comma
          output.write('\n');
        } else {
          output.write(',');
          if (next == JSONStreamReader.Event.START_ELEMENT_KEY) {
            output.write('\n');
          } else {
            output.write(' ');
          }
        }
        input.back(); // reset

      } else if (event == JSONStreamReader.Event.START_ELEMENT_KEY) {
        addIndentation(output, indentation);
        output.write('"');
        output.write(JSONObject.escape(input.getStringValue()));
        output.write('"');
        output.write(" : ");

      } else if (event == JSONStreamReader.Event.START_ELEMENT_VALUE) {
        if (Boolean.class.equals(input.getObjectValue().getClass())) {
          output.write(String.valueOf(input.getBooleanValue()));

        } else if (String.class.equals(input.getObjectValue().getClass())) {
          output.write('"');
          output.write(JSONObject.escape(input.getStringValue()));
          output.write('"');

        } else if (Double.class.equals(input.getObjectValue().getClass())) {
          output.write(df.format(input.getDoubleValue()));
        } else if (Long.class.equals(input.getObjectValue().getClass())) {
          output.write(String.valueOf(input.getLongValue()));
        } else {
          throw new RuntimeException("Unsupported class: " + input.getObjectValue().getClass());
        }

        if (input.next() != JSONStreamReader.Event.NEXT_VALUE) {
          output.write("\n");
        }
        input.back(); // reset

      } else if (event == JSONStreamReader.Event.END_DOCUMENT) {
        break;
      } else {
        throw new RuntimeException("Unexpected event " + event);
      }
    }

  }

  public void addIndentation(Writer output, int indentation) throws IOException {
    for (int i = 0; i < indentation; i++) {
      output.append("  ");
    }
  }

}
