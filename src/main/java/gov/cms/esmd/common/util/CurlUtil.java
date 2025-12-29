package gov.cms.esmd.common.util;

import java.util.Map;

public final class CurlUtil {

    private CurlUtil() {}

    public static String toCurl(
            String baseURL,
            String endpoint,
            String httpMethod,
            Map<String, String> headers,
            Object data,
            String paramQuery
    ) {
        // Method resolution
        String method = (httpMethod != null ? httpMethod : "GET").toUpperCase();

        // Build URL
        String cleanBaseURL = (baseURL != null) ? baseURL.replaceAll("/$", "") : "";
        String finalEndpoint = (endpoint != null) ? endpoint : "";
        String url = cleanBaseURL + finalEndpoint;

        StringBuilder curl = new StringBuilder();
        curl.append("curl -X ").append(method);

        // Add headers
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                curl.append(" -H \"")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\"");
            }
        }

        // Add data
        if (data != null) {
            String dataStr;

            if (data instanceof String) {
                dataStr = (String) data;
            } else {
                // Convert object to JSON-like string
                dataStr = JsonUtil.toJson(data);
            }

            curl.append(" --data '")
                    .append(dataStr)
                    .append("'");
        }

        // Append query string if provided
        if (paramQuery == null) paramQuery = "";

        curl.append(" \"")
                .append(url)
                .append(paramQuery)
                .append("\"");

        return curl.toString();
    }

    public static String buildGetCurl(String url, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder("curl -X GET '").append(url).append("'");
        headers.forEach((k, v) -> sb.append(" -H '").append(k).append(": ").append(v).append("'"));
        return sb.toString();
    }
}
