package gov.cms.esmd.common.util;

import java.util.UUID;

public final class GuidUtil {

    // Private constructor to prevent instantiation
    private GuidUtil() {
    }

    // Generate UUID like Node.js GuidGenerator.generate()
    public static String generateGuid() {
        return UUID.randomUUID().toString();
    }
}
