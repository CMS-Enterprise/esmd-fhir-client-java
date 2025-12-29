package gov.cms.esmd.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtil {

    private FileUtil() {
        // Utility class
    }

    /**
     * Writes text content to a JSON file inside the target folder.
     *
     * @param baseFolder The base directory where files will be stored
     * @param folder     Subfolder inside the base directory
     * @param filename   Filename without extension
     * @param content    File content
     */
    public static void writeJsonFile(String baseFolder, String folder, String filename, String content) {
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
    public static String getFullFilePath(String folder, String fileName) {
        if (folder == null || folder.isEmpty() ||
                fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Folder and fileName are required.");
        }

        Path fullPath = Paths.get(folder, fileName).toAbsolutePath();
        return fullPath.toString();
    }

    /**
     * Gets the file size in MB (rounded to two decimals).
     */
    public static double getFileSizeInMB(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + filePath);
        }

        try {
            long bytes = Files.size(path);
            double mb = bytes / (1024.0 * 1024.0);
            return Math.round(mb * 100.0) / 100.0; // round to 2 decimals
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file: " + filePath, e);
        }
    }

    public static long getFileSizeBytes(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new RuntimeException("File not found: " + filePath);
        }

        return file.length();
    }


}

