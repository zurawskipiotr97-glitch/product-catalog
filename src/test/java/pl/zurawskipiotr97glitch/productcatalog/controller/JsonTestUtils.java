package pl.zurawskipiotr97glitch.productcatalog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static long extractId(String json) throws Exception {
        JsonNode node = objectMapper.readTree(json);
        return node.get("id").asLong();
    }
}