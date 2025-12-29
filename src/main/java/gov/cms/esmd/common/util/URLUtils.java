package gov.cms.esmd.common.util;


import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class URLUtils {

    /**
     * Equivalent to the TS parseUrl() function.
     */
    public static ParsedUrl parseUrl(String fullUrl) {

        try {
            URI uri = new URI(fullUrl);

            String baseUrl = uri.getScheme() + "://" + uri.getHost()
                    + ((uri.getPort() != -1) ? (":" + uri.getPort()) : "");

            String resourcePath = uri.getPath();

            Map<String, String> params = splitQuery(uri);

            return new ParsedUrl(baseUrl, resourcePath, params);

        } catch (Exception e) {
            throw new RuntimeException("Invalid URL: " + fullUrl, e);
        }
    }

    /**
     * Extracts query parameters into a Map<String,String>
     */
    public static Map<String, String> getQueryParams(String url) {
        try {
            URI uri = new URI(url);
            return splitQuery(uri);
        } catch (Exception e) {
            throw new RuntimeException("Invalid URL: " + url, e);
        }
    }

    /**
     * Convert params map to ?a=b&c=d
     */
    public static String paramsToQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("?");

        boolean first = true;
        for (var entry : params.entrySet()) {
            if (!first) sb.append("&");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        return sb.toString();
    }

    /**
     * Helper to convert query params into a map.
     */
    private static Map<String, String> splitQuery(URI uri) {
        Map<String, String> map = new LinkedHashMap<>();
        if (uri.getQuery() == null) return map;

        String[] pairs = uri.getQuery().split("&");

        for (String pair : pairs) {
            if (!pair.contains("=")) continue;

            String[] kv = pair.split("=", 2);
            map.put(kv[0], kv.length > 1 ? kv[1] : "");
        }

        return map;
    }

    /**
     * Helper function for building query strings (like TS).
     */
    public static String toQueryString(Map<String, String> params) {
        return paramsToQueryString(params);
    }
}
