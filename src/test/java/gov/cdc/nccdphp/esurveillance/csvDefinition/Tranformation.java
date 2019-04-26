package gov.cdc.nccdphp.esurveillance.csvDefinition;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import gov.cdc.nccdphp.esurveillance.utils.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Created - 11/11/18
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
public class Tranformation {

    @Test
    public void testConversion() {

        ReadContext ctx = JsonPath.parse(json);

        List<String> fields = ctx.read("$.fields[*].fieldItem");
        Map<String, String[]> newDoc = new HashMap<>();
        for (String field: fields) {
            newDoc.put(field, ctx.read("$.fields[?(@.fieldItem == '" + field + "')].values"));
        }
        System.out.println(Arrays.asList(newDoc));
    }


    @Test
    public void testParsingFieldValuesWithDots() {
        String example="1231.20.3...  4 456  70";

        String[] splitted = example.split("(?<=\\G.{" + 3 + "})");
        String[] newArray  = Arrays.stream(splitted).map(v -> StringUtils.trim(v.trim(), "\\.")).toArray(String[]::new);

        Arrays.stream(newArray).forEach(System.out::println);

    }

        String json = "{\n" +
                "            \"rowNumber\": 1,\n" +
                "            \"fields\": [\n" +
                "                {\n" +
                "                    \"fieldNumber\": 71,\n" +
                "                    \"fieldItem\": \"17f\",\n" +
                "                    \"values\": [\n" +
                "                        \"2\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 82,\n" +
                "                    \"fieldItem\": \"21a\",\n" +
                "                    \"values\": [\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 83,\n" +
                "                    \"fieldItem\": \"21b\",\n" +
                "                    \"values\": [\n" +
                "                        \"9\",\n" +
                "                        \" \",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 84,\n" +
                "                    \"fieldItem\": \"21c\",\n" +
                "                    \"values\": [\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 31,\n" +
                "                    \"fieldItem\": \"7a\",\n" +
                "                    \"values\": [\n" +
                "                        \"01\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 32,\n" +
                "                    \"fieldItem\": \"7b\",\n" +
                "                    \"values\": [\n" +
                "                        \"02\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 33,\n" +
                "                    \"fieldItem\": \"7c\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 34,\n" +
                "                    \"fieldItem\": \"7d\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 9,\n" +
                "                    \"fieldItem\": \"3a\",\n" +
                "                    \"values\": [\n" +
                "                        \"60559          \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 35,\n" +
                "                    \"fieldItem\": \"7e\",\n" +
                "                    \"values\": [\n" +
                "                        \"2\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 10,\n" +
                "                    \"fieldItem\": \"3b\",\n" +
                "                    \"values\": [\n" +
                "                        \"18039\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 36,\n" +
                "                    \"fieldItem\": \"7f\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 11,\n" +
                "                    \"fieldItem\": \"3c\",\n" +
                "                    \"values\": [\n" +
                "                        \"46516\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 12,\n" +
                "                    \"fieldItem\": \"3d\",\n" +
                "                    \"values\": [\n" +
                "                        \"071972\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 13,\n" +
                "                    \"fieldItem\": \"3e\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 14,\n" +
                "                    \"fieldItem\": \"3f\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 15,\n" +
                "                    \"fieldItem\": \"3g\",\n" +
                "                    \"values\": [\n" +
                "                        \"7\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 16,\n" +
                "                    \"fieldItem\": \"3h\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 17,\n" +
                "                    \"fieldItem\": \"3i\",\n" +
                "                    \"values\": [\n" +
                "                        \"02\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 72,\n" +
                "                    \"fieldItem\": \"18a\",\n" +
                "                    \"values\": [\n" +
                "                        \"03112014\",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 73,\n" +
                "                    \"fieldItem\": \"18b\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 54,\n" +
                "                    \"fieldItem\": \"14a\",\n" +
                "                    \"values\": [\n" +
                "                        \"03112014\",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 55,\n" +
                "                    \"fieldItem\": \"14b\",\n" +
                "                    \"values\": [\n" +
                "                        \"300\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 56,\n" +
                "                    \"fieldItem\": \"14c\",\n" +
                "                    \"values\": [\n" +
                "                        \"066\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 57,\n" +
                "                    \"fieldItem\": \"14d\",\n" +
                "                    \"values\": [\n" +
                "                        \"150\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 41,\n" +
                "                    \"fieldItem\": \"10a\",\n" +
                "                    \"values\": [\n" +
                "                        \"77\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 58,\n" +
                "                    \"fieldItem\": \"14e\",\n" +
                "                    \"values\": [\n" +
                "                        \"0250\",\n" +
                "                        \"    \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 42,\n" +
                "                    \"fieldItem\": \"10b\",\n" +
                "                    \"values\": [\n" +
                "                        \"00\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 43,\n" +
                "                    \"fieldItem\": \"10c\",\n" +
                "                    \"values\": [\n" +
                "                        \"00\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 37,\n" +
                "                    \"fieldItem\": \"8a\",\n" +
                "                    \"values\": [\n" +
                "                        \"030\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 38,\n" +
                "                    \"fieldItem\": \"8b\",\n" +
                "                    \"values\": [\n" +
                "                        \"060\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 18,\n" +
                "                    \"fieldItem\": \"4a\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 19,\n" +
                "                    \"fieldItem\": \"4b\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 20,\n" +
                "                    \"fieldItem\": \"4c\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 21,\n" +
                "                    \"fieldItem\": \"4d\",\n" +
                "                    \"values\": [\n" +
                "                        \"2\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 1,\n" +
                "                    \"fieldItem\": \"0a\",\n" +
                "                    \"values\": [\n" +
                "                        \"900\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 74,\n" +
                "                    \"fieldItem\": \"19a\",\n" +
                "                    \"values\": [\n" +
                "                        \"        \",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 59,\n" +
                "                    \"fieldItem\": \"15a\",\n" +
                "                    \"values\": [\n" +
                "                        \"03112014\",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 60,\n" +
                "                    \"fieldItem\": \"15b\",\n" +
                "                    \"values\": [\n" +
                "                        \"999\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 61,\n" +
                "                    \"fieldItem\": \"15c\",\n" +
                "                    \"values\": [\n" +
                "                        \"08.9\",\n" +
                "                        \"    \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 44,\n" +
                "                    \"fieldItem\": \"11a\",\n" +
                "                    \"values\": [\n" +
                "                        \"61\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 45,\n" +
                "                    \"fieldItem\": \"11b\",\n" +
                "                    \"values\": [\n" +
                "                        \"177\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 46,\n" +
                "                    \"fieldItem\": \"11c\",\n" +
                "                    \"values\": [\n" +
                "                        \"39\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 47,\n" +
                "                    \"fieldItem\": \"11d\",\n" +
                "                    \"values\": [\n" +
                "                        \"41\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 39,\n" +
                "                    \"fieldItem\": \"9a\",\n" +
                "                    \"values\": [\n" +
                "                        \"4\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 40,\n" +
                "                    \"fieldItem\": \"9b\",\n" +
                "                    \"values\": [\n" +
                "                        \"00\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 22,\n" +
                "                    \"fieldItem\": \"5a\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 23,\n" +
                "                    \"fieldItem\": \"5b\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 24,\n" +
                "                    \"fieldItem\": \"5c\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 25,\n" +
                "                    \"fieldItem\": \"5d\",\n" +
                "                    \"values\": [\n" +
                "                        \"07\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 2,\n" +
                "                    \"fieldItem\": \"1a\",\n" +
                "                    \"values\": [\n" +
                "                        \"18\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 26,\n" +
                "                    \"fieldItem\": \"5e\",\n" +
                "                    \"values\": [\n" +
                "                        \"07\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 3,\n" +
                "                    \"fieldItem\": \"1b\",\n" +
                "                    \"values\": [\n" +
                "                        \"18039\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 27,\n" +
                "                    \"fieldItem\": \"5f\",\n" +
                "                    \"values\": [\n" +
                "                        \"07\",\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 4,\n" +
                "                    \"fieldItem\": \"1c\",\n" +
                "                    \"values\": [\n" +
                "                        \"46516\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 5,\n" +
                "                    \"fieldItem\": \"1d\",\n" +
                "                    \"values\": [\n" +
                "                        \"1770928343\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 62,\n" +
                "                    \"fieldItem\": \"16a\",\n" +
                "                    \"values\": [\n" +
                "                        \"3\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 63,\n" +
                "                    \"fieldItem\": \"16b\",\n" +
                "                    \"values\": [\n" +
                "                        \"        \",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 64,\n" +
                "                    \"fieldItem\": \"16c\",\n" +
                "                    \"values\": [\n" +
                "                        \"3\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 65,\n" +
                "                    \"fieldItem\": \"16d\",\n" +
                "                    \"values\": [\n" +
                "                        \"        \",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 48,\n" +
                "                    \"fieldItem\": \"12a\",\n" +
                "                    \"values\": [\n" +
                "                        \"03112014\",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 49,\n" +
                "                    \"fieldItem\": \"12b\",\n" +
                "                    \"values\": [\n" +
                "                        \"130\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 50,\n" +
                "                    \"fieldItem\": \"12c\",\n" +
                "                    \"values\": [\n" +
                "                        \"084\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 51,\n" +
                "                    \"fieldItem\": \"12d\",\n" +
                "                    \"values\": [\n" +
                "                        \"130\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 75,\n" +
                "                    \"fieldItem\": \"20a\",\n" +
                "                    \"values\": [\n" +
                "                        \"  \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 52,\n" +
                "                    \"fieldItem\": \"12e\",\n" +
                "                    \"values\": [\n" +
                "                        \"084\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 76,\n" +
                "                    \"fieldItem\": \"20b\",\n" +
                "                    \"values\": [\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 77,\n" +
                "                    \"fieldItem\": \"20c\",\n" +
                "                    \"values\": [\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \",\n" +
                "                        \"          \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 78,\n" +
                "                    \"fieldItem\": \"20d\",\n" +
                "                    \"values\": [\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 79,\n" +
                "                    \"fieldItem\": \"20e\",\n" +
                "                    \"values\": [\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 80,\n" +
                "                    \"fieldItem\": \"20f\",\n" +
                "                    \"values\": [\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 81,\n" +
                "                    \"fieldItem\": \"20g\",\n" +
                "                    \"values\": [\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 28,\n" +
                "                    \"fieldItem\": \"6a\",\n" +
                "                    \"values\": [\n" +
                "                        \"004\",\n" +
                "                        \"   \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 29,\n" +
                "                    \"fieldItem\": \"6b\",\n" +
                "                    \"values\": [\n" +
                "                        \"6\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 30,\n" +
                "                    \"fieldItem\": \"6c\",\n" +
                "                    \"values\": [\n" +
                "                        \"5\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 6,\n" +
                "                    \"fieldItem\": \"2a\",\n" +
                "                    \"values\": [\n" +
                "                        \"2\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 7,\n" +
                "                    \"fieldItem\": \"2b\",\n" +
                "                    \"values\": [\n" +
                "                        \"01\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 8,\n" +
                "                    \"fieldItem\": \"2c\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 66,\n" +
                "                    \"fieldItem\": \"17a\",\n" +
                "                    \"values\": [\n" +
                "                        \"03112014\",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 67,\n" +
                "                    \"fieldItem\": \"17b\",\n" +
                "                    \"values\": [\n" +
                "                        \"03112014\",\n" +
                "                        \"        \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 68,\n" +
                "                    \"fieldItem\": \"17c\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 69,\n" +
                "                    \"fieldItem\": \"17d\",\n" +
                "                    \"values\": [\n" +
                "                        \"2\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 53,\n" +
                "                    \"fieldItem\": \"13a\",\n" +
                "                    \"values\": [\n" +
                "                        \"1\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"fieldNumber\": 70,\n" +
                "                    \"fieldItem\": \"17e\",\n" +
                "                    \"values\": [\n" +
                "                        \"2\",\n" +
                "                        \" \"\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }";



}
