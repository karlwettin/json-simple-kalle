package org.json.simple.parser;

import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author kalle
 * @since 2012-04-06 01:18
 */
public class TestJSONFormatter extends TestCase {

  public void test() throws Exception {



    String semiWellFormatted = "{\n" +
        "  \"boolean\" : false,\n" +
        "  \"long\" : 1,\n" +
        "  \"null\" : null,\n" +
        "  \"string\" : \"räksmörgås\",\n" +
        "  \"array\" : [\n" +
        "    {\n" +
        "      \"item\" : 1\n" +
        "    }, {\n" +
        "      \"item\" : 2\n" +
        "      \"deeper\" : {\n" +
        "        \"boolean\" : false,\n" +
        "        \"long\" : 1,\n" +
        "        \"string\" : \"räksmörgås\",\n" +
        "      }\n" +
        "    }, {\n" +
        "      \"item\" : 3\n" +
        "    }\n" +
        "  ],\n" +
        "}";

    String expected = "{\n" +
        "  \"boolean\" : false,\n" +
        "  \"long\" : 1,\n" +
        "  \"null\" : null,\n" +
        "  \"string\" : \"räksmörgås\",\n" +
        "  \"array\" : [\n" +
        "    {\n" +
        "      \"item\" : 1\n" +
        "    }, {\n" +
        "      \"item\" : 2\n" +
        "      \"deeper\" : {\n" +
        "        \"boolean\" : false,\n" +
        "        \"long\" : 1,\n" +
        "        \"string\" : \"räksmörgås\"\n" +
        "      }\n" +
        "    }, {\n" +
        "      \"item\" : 3\n" +
        "    }\n" +
        "  ]\n" +
        "}\n";


    JSONFormatter formatter = new JSONFormatter();

    assertEquals(expected, formatter.format(semiWellFormatted.replaceAll("\\s+", "")));

  }

}
