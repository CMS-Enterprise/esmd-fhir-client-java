package gov.cms.esmd.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JSONUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtility.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static String serialize(Object object) {
        LOGGER.info("Start JSONUtility:serialize(Object)....");
        if (object == null) {
            return null;
        }
        LOGGER.info("End JSONUtility:serialize(Object)....");
        return gson.toJson(object);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        LOGGER.info("Start JSONUtility:deserialize(json, class)....");
        if (json == null || clazz == null) {
            return null;
        }
        LOGGER.info("End JSONUtility:deserialize(json, class)....");
        return gson.fromJson(json, clazz);
    }
    // Serialize an object to JSON string
    public static String toJson(Object obj) throws JsonProcessingException {
        return DEFAULT_MAPPER.writeValueAsString(obj);
    }

    // Deserialize a JSON string to an object
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return DEFAULT_MAPPER.readValue(json, clazz);
    }

    // Deserialize a JSON string to a generic type (like List<T>)
    public static <T> T fromJson(String json, TypeReference<T> typeReference) throws JsonProcessingException {
        return DEFAULT_MAPPER.readValue(json, typeReference);
    }

    // Parse JSON string into a dynamic ObjectNode
    public static ObjectNode parseJsonToDynamic(String json) {
        try {
            return (ObjectNode) DEFAULT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            // log or handle error if needed
            return null;
        }
    }

    // Load object from JSON file asynchronously (Java doesn't have async file read, using blocking)
    public static <T> T loadFromJsonFile(File file, Class<T> clazz) throws IOException {
        if (!file.exists()) throw new IOException("JSON file not found: " + file.getAbsolutePath());
        String jsonString = Files.readString(file.toPath());
        return fromJson(jsonString, clazz);
    }

    // Load all JSON files from a folder into a List<T>
    public static <T> List<T> loadFromJsonFolder(File folder, Class<T> clazz, String searchPattern) throws IOException {
        if (!folder.exists() || !folder.isDirectory())
            throw new IOException("Folder not found: " + folder.getAbsolutePath());

        List<T> result = new ArrayList<>();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    result.add(loadFromJsonFile(file, clazz));
                } catch (Exception ex) {
                    System.err.println("Error loading file '" + file.getAbsolutePath() + "': " + ex.getMessage());
                }
            }
        }
        return result;
    }

}
