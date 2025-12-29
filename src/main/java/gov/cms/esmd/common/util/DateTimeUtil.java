package gov.cms.esmd.common.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {

    private static final DateTimeFormatter UTC_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .withZone(ZoneOffset.UTC);

    private static final DateTimeFormatter OFFSET_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * Returns current UTC time in yyyy-MM-ddTHH:mm:ssZ format
     */
    public static String getCurrentUtc() {
        return UTC_FORMATTER.format(Instant.now());
    }

    /**
     * Formats a given Date as UTC in yyyy-MM-ddTHH:mm:ssZ format
     */
    public static String formatAsUtc(Date dateTime) {
        return UTC_FORMATTER.format(dateTime.toInstant());
    }

    /**
     * Returns current local date/time with offset in yyyy-MM-ddTHH:mm:ss±HH:mm format
     */
    public static String getCurrentWithOffset() {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
        return OFFSET_FORMATTER.format(now);
    }
}
