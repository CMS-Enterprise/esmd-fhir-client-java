package gov.cms.esmd.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class DataFileWriterUtils {

    private final String baseFolder;
    private final ObjectMapper mapper = new ObjectMapper();


    public DataFileWriterUtils(String baseFolder) {
        this.baseFolder = baseFolder;

    }

    /**
     * Creates a subfolder and writes JSON data to a file inside it.
     * Accepts either an Object or a pre-formatted JSON string.
     *
     * @param subFolder Subdirectory name
     * @param fileName  File name without .json
     * @param data      Object or JSON string
     */
    public void writeJson(String subFolder, String fileName, Object data) {
        try {
            Path fullPath = Paths.get(baseFolder, subFolder);
            Files.createDirectories(fullPath);

            Path filePath = fullPath.resolve(fileName + ".json");

            String jsonData;

            if (data instanceof String) {
                String jsonString = (String) data;
                // Validate JSON
                try {
                    mapper.readTree(jsonString);
                    jsonData = jsonString;
                } catch (Exception e) {
                    log.error("Provided string is not valid JSON: {}", e.getMessage());
                    return;
                }
            } else {
                // Convert object to JSON
                jsonData = mapper.writeValueAsString(data);
            }

            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(jsonData);
            }

            log.info("JSON file created at: {}", filePath.toAbsolutePath());

        } catch (IOException e) {
            log.error("Error writing JSON file: {}", e.getMessage(), e);
        }
    }

    /**
     * Creates a subfolder and writes plain text to a .txt file inside it.
     *
     * @param subFolder Subdirectory name
     * @param fileName  File name without .txt
     * @param text      Plain text content
     */
    public void writeText(String subFolder, String fileName, String text) {
        try {
            Path fullPath = Paths.get(baseFolder, subFolder);
            Files.createDirectories(fullPath);

            Path filePath = fullPath.resolve(fileName + ".txt");

            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(text);
            }

            log.info("Text file created at: {}", filePath.toAbsolutePath());

        } catch (IOException e) {
            log.error("Error writing text file: {}", e.getMessage(), e);
        }
    }

    public void writeJsonFile(String baseFolder, String folder, String filename, String content) {
        File dir = new File(baseFolder, folder);

        if (!dir.exists() && dir.mkdirs()) {
            return;
        }

        File file = new File(dir, filename + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + file.getAbsolutePath(), e);
        }

    }
}
