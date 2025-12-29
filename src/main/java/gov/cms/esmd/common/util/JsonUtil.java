package gov.cms.esmd.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public final class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    /**
     * Converts an object to JSON, performs global replacements, converts back into the same type.
     *
     * @param originalObject the object to process
     * @param replacements   map of string-to-string replacements
     * @param type           the class of T (avoids unchecked cast)
     * @param <T>            object type
     * @return updated object
     */
    public static <T> T replaceValues(
            T originalObject,
            Map<String, String> replacements,
            Class<T> type
    ) {
        try {
            // Convert to JSON
            String json = mapper.writeValueAsString(originalObject);

            // Apply global replacements
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                json = json.replace(entry.getKey(), entry.getValue());
            }

            // Deserialize back into the provided type
            return mapper.readValue(json, type);

        } catch (Exception e) {
            throw new RuntimeException("Failed to replace JSON values", e);
        }
    }

    public static ObjectNode toObjectNode(Object obj) {
        JsonNode node = mapper.valueToTree(obj);

        if (!(node instanceof ObjectNode)) {
            throw new IllegalArgumentException("Provided object does not convert to an ObjectNode.");
        }

        return (ObjectNode) node;
    }
}
