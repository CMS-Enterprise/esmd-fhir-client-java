package gov.cms.esmd.utilities;

import gov.cms.esmd.utilities.Constants;
import org.yaml.snakeyaml.Yaml;
import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.util.*;



public class PropertiesUtils {

    private static Properties properties ;
    public static Properties loadProperties() {
        if( properties == null ){
            properties = loadProperties(null);
        }
        return properties;
    }

    public static Properties loadProperties(String profile) {
        try {
            Yaml yaml = new Yaml();
            Properties properties = new Properties();
            properties.putAll(getFlattenedMap(yaml.load(resourceToString("/api-properties.yml", defaultCharset()))));
            return properties;
        } catch (IOException e) {
            throw new Error("Cannot load properties", e);
        }
    }

    private static final Map<String, Object> getFlattenedMap(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap(result, source, null);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
        source.forEach((key, value) -> {
            if (!isBlank(path))
                key = path + (key.startsWith("[") ? key : '.' + key);
            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                buildFlattenedMap(result, (Map<String, Object>) value, key);
            } else if (value instanceof Collection) {
                int count = 0;
                for (Object object : (Collection<?>) value)
                    buildFlattenedMap(result, Collections.singletonMap("[" + (count++) + "]", object), key);
            } else {
                result.put(key, value != null ? "" + value : "");
            }
        });
    }
    public static String constructBaseUrl(Properties properties, String environment) {

        String baseUrl;
        if (environment != null && environment.equalsIgnoreCase("PROD")) {
            baseUrl = properties.getProperty("api.environment.prod");
        } else if (environment != null && environment.equalsIgnoreCase("UAT")) {
            baseUrl = properties.getProperty("api.environment.uat");
        } else if (environment != null && environment.equalsIgnoreCase("VAL")) {
            baseUrl = properties.getProperty("api.environment.val");
        } else {
            baseUrl = properties.getProperty(Constants.BASEURLKEY);
        }
        return baseUrl;
    }
  /*public static void main(String[] args) throws Exception {
    Properties properties = loadProperties();
    System.out.println(properties.get("api.environment.uat"));

    ArrayList<Notification> ntst = new ArrayList<>();
    ntst.add(new Notification());

    AdminErrorNotificationRoot aenr = new AdminErrorNotificationRoot();
    aenr.setNotification(ntst);

    ArrayList<AdminErrorNotificationRoot> tst = new ArrayList<>();
    tst.add(aenr);

    tst.get(0)
        .getNotification()
        .forEach(
            notification -> {
              //                OffsetDateTime currentDateTime = OffsetDateTime.now();
              //              String formattedDateTime =
              //
              // currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"));
              //                notification.setCreationTime(new
              // Date(Long.parseLong(formattedDateTime.substring(0, formattedDateTime.length() -
              // 3))));
              //                notification.setSubmissionTime(new
              // Date(Long.parseLong(formattedDateTime.substring(0, formattedDateTime.length() -
              // 3))));

              notification.setCreationTime(NotificationUtility.convertDateToString(new Date(), Constants.DATEFORMAT));
              ;
            });
    String jsonString = new Gson().toJson(aenr);
    System.out.println(tst.get(0).getNotification().get(0).getCreationTime());
    System.out.println(jsonString);
  }*/
}
