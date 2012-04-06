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



    String semiWellFormatted = "{ \"request\" : { \"success\" : true }, \"response\" : {\n" +
        "  \"identity\" : \"00000302-2bdf-4458-af9a-978fd4d72ac0\",\n" +
        "  \"namn\" : {\n" +
        "    \"value\" : \"Nick's Bar & Restaurant Aktiebolag\",\n" +
        "    \"lastSeen\" : 1330766391921,\n" +
        "    \"firstSeen\" : 1330766391921,\n" +
        "    \"trustworthiness\" : null,\n" +
        "    \"sources\" : {\n" +
        "      \"authors\" : [\n" +
        "\"Bolagsverket\\/Näringslivsregistret\"\n" +
        "      ],\n" +
        "      \"licenses\" : [\n" +
        "\"public domain\"\n" +
        "      ]\n" +
        "    }\n" +
        "  },\n" +
        "  \"nummerPrefix\" : null,\n" +
        "  \"nummer\" : {\n" +
        "    \"value\" : \"5563875037\",\n" +
        "    \"lastSeen\" : 1330766391921,\n" +
        "    \"firstSeen\" : 1330766391921,\n" +
        "    \"trustworthiness\" : 1.0,\n" +
        "    \"sources\" : {\n" +
        "      \"authors\" : [\n" +
        "\"Bolagsverket\\/Näringslivsregistret\"\n" +
        "      ],\n" +
        "      \"licenses\" : [\n" +
        "\"public domain\"\n" +
        "      ]\n" +
        "    }\n" +
        "  },\n" +
        "  \"nummerSuffix\" : null,\n" +
        "  \"firmaform\" : {\n" +
        "    \"value\" : \"AB\",\n" +
        "    \"lastSeen\" : 1330766391921,\n" +
        "    \"firstSeen\" : 1330766391921,\n" +
        "    \"trustworthiness\" : 1.0,\n" +
        "    \"sources\" : {\n" +
        "      \"authors\" : [\n" +
        "\"Bolagsverket\\/Näringslivsregistret\"\n" +
        "      ],\n" +
        "      \"licenses\" : [\n" +
        "\"public domain\"\n" +
        "      ]\n" +
        "    }\n" +
        "  },\n" +
        "  \"lanIdentity\" : {\n" +
        "    \"value\" : \"0ad7061f-536a-42dc-b26b-b01df36f422b\",\n" +
        "    \"lastSeen\" : 1330766391921,\n" +
        "    \"firstSeen\" : 1330766391921,\n" +
        "    \"trustworthiness\" : 1.0,\n" +
        "    \"sources\" : {\n" +
        "      \"authors\" : [\n" +
        "\"Bolagsverket\\/Näringslivsregistret\"\n" +
        "      ],\n" +
        "      \"licenses\" : [\n" +
        "\"public domain\"\n" +
        "      ]\n" +
        "    }\n" +
        "  },\n" +
        "  \"status\" : [\n" +
        "    {\n" +
        "      \"value\" : \"Konkurs avslutad\",\n" +
        "      \"lastSeen\" : 1330766391921,\n" +
        "      \"firstSeen\" : 1330766391921,\n" +
        "      \"trustworthiness\" : 1.0,\n" +
        "      \"sources\" : {\n" +
        "        \"authors\" : [\n" +
        "\"Bolagsverket\\/Näringslivsregistret\"\n" +
        "        ],\n" +
        "        \"licenses\" : [\n" +
        "\"public domain\"\n" +
        "        ]\n" +
        "      }\n" +
        "    }\n" +
        "  ],\n" +
        "  \"lastSeen\" : 1330766391921,\n" +
        "  \"firstSeen\" : 1330766391921,\n" +
        "  \"trustworthiness\" : 1.0,\n" +
        "  \"sources\" : {\n" +
        "    \"authors\" : [\n" +
        "\"Bolagsverket\\/Näringslivsregistret\"\n" +
        "    ],\n" +
        "    \"licenses\" : [\n" +
        "\"public domain\",\n" +
        "    ]\n" +
        "  }\n" +
        "}\n" +
        "}";

    String expected = "{\n" +
        "  \"request\" : {\n" +
        "    \"success\" : true\n" +
        "  },\n" +
        "  \"response\" : {\n" +
        "    \"identity\" : \"00000302-2bdf-4458-af9a-978fd4d72ac0\",\n" +
        "    \"namn\" : {\n" +
        "      \"value\" : \"Nick's Bar & Restaurant Aktiebolag\",\n" +
        "      \"lastSeen\" : 1330766391921,\n" +
        "      \"firstSeen\" : 1330766391921,\n" +
        "      \"trustworthiness\" : null,\n" +
        "      \"sources\" : {\n" +
        "        \"authors\" : [\n" +
        "          \"Bolagsverket\\/Näringslivsregistret\"\n" +
        "        ],\n" +
        "        \"licenses\" : [\n" +
        "          \"public domain\"\n" +
        "        ]\n" +
        "      }\n" +
        "    },\n" +
        "    \"nummerPrefix\" : null,\n" +
        "    \"nummer\" : {\n" +
        "      \"value\" : \"5563875037\",\n" +
        "      \"lastSeen\" : 1330766391921,\n" +
        "      \"firstSeen\" : 1330766391921,\n" +
        "      \"trustworthiness\" : 1.0,\n" +
        "      \"sources\" : {\n" +
        "        \"authors\" : [\n" +
        "          \"Bolagsverket\\/Näringslivsregistret\"\n" +
        "        ],\n" +
        "        \"licenses\" : [\n" +
        "          \"public domain\"\n" +
        "        ]\n" +
        "      }\n" +
        "    },\n" +
        "    \"nummerSuffix\" : null,\n" +
        "    \"firmaform\" : {\n" +
        "      \"value\" : \"AB\",\n" +
        "      \"lastSeen\" : 1330766391921,\n" +
        "      \"firstSeen\" : 1330766391921,\n" +
        "      \"trustworthiness\" : 1.0,\n" +
        "      \"sources\" : {\n" +
        "        \"authors\" : [\n" +
        "          \"Bolagsverket\\/Näringslivsregistret\"\n" +
        "        ],\n" +
        "        \"licenses\" : [\n" +
        "          \"public domain\"\n" +
        "        ]\n" +
        "      }\n" +
        "    },\n" +
        "    \"lanIdentity\" : {\n" +
        "      \"value\" : \"0ad7061f-536a-42dc-b26b-b01df36f422b\",\n" +
        "      \"lastSeen\" : 1330766391921,\n" +
        "      \"firstSeen\" : 1330766391921,\n" +
        "      \"trustworthiness\" : 1.0,\n" +
        "      \"sources\" : {\n" +
        "        \"authors\" : [\n" +
        "          \"Bolagsverket\\/Näringslivsregistret\"\n" +
        "        ],\n" +
        "        \"licenses\" : [\n" +
        "          \"public domain\"\n" +
        "        ]\n" +
        "      }\n" +
        "    },\n" +
        "    \"status\" : [\n" +
        "      {\n" +
        "        \"value\" : \"Konkurs avslutad\",\n" +
        "        \"lastSeen\" : 1330766391921,\n" +
        "        \"firstSeen\" : 1330766391921,\n" +
        "        \"trustworthiness\" : 1.0,\n" +
        "        \"sources\" : {\n" +
        "          \"authors\" : [\n" +
        "            \"Bolagsverket\\/Näringslivsregistret\"\n" +
        "          ],\n" +
        "          \"licenses\" : [\n" +
        "            \"public domain\"\n" +
        "          ]\n" +
        "        }\n" +
        "      }\n" +
        "    ],\n" +
        "    \"lastSeen\" : 1330766391921,\n" +
        "    \"firstSeen\" : 1330766391921,\n" +
        "    \"trustworthiness\" : 1.0,\n" +
        "    \"sources\" : {\n" +
        "      \"authors\" : [\n" +
        "        \"Bolagsverket\\/Näringslivsregistret\"\n" +
        "      ],\n" +
        "      \"licenses\" : [\n" +
        "        \"public domain\"\n" +
        "      ]\n" +
        "    }\n" +
        "  }\n" +
        "}\n";


    JSONFormatter formatter = new JSONFormatter();

    assertEquals(expected, formatter.format(semiWellFormatted));

  }

}
