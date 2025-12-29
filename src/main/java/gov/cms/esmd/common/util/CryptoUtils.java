package gov.cms.esmd.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoUtils {

    /**
     * Computes the MD5 hash of a file and returns it as a Base64 string.
     */
    public static String computeContentMd5String(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + filePath);
        }

        try {
            byte[] fileBytes = Files.readAllBytes(path);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(fileBytes);

            return Base64.getEncoder().encodeToString(digest);

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to compute MD5 for file: " + filePath, e);
        }
    }

    /**
     * Computes the MD5 hash of a file and returns it as a byte array.
     */
    public static byte[] computeContentMd5Bytes(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + filePath);
        }

        try {
            byte[] fileBytes = Files.readAllBytes(path);
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(fileBytes);

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to compute MD5 for file: " + filePath, e);
        }
    }

    /**
     * Converts a Base64 string into a byte array.
     * Equivalent to Node.js Buffer.from(base64, 'base64').
     */
    public static byte[] convertBase64StringToBytes(String base64StringValue) {
        return Base64.getDecoder().decode(base64StringValue);
    }

    /**
     * Computes the SHA-256 checksum of a file and returns it as a lowercase hex string.
     * Equivalent to Node.js crypto.createHash('sha256').digest('hex').
     */
    public static String computeSHA256Checksum(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + filePath);
        }

        try {
            byte[] fileBytes = Files.readAllBytes(path);
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha256.digest(fileBytes);

            // Convert bytes to hex string (lowercase)
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString(); // already lowercase

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to compute SHA-256 checksum for: " + filePath, e);
        }
    }
}
