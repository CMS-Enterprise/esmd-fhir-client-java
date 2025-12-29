package gov.cms.esmd.notification.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cms.esmd.common.model.ClientResponse;

import gov.cms.esmd.common.util.*;
import gov.cms.esmd.config.model.AppConfigJackson;
import gov.cms.esmd.notification.model.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NotificationAPIHandler {

    private final AppConfigJackson.AppSettings.NotificationRetrievalAPI settings;
    private final AppConfigJackson.AppSettings appSettings;
    private final String token;
    private final ObjectMapper mapper = new ObjectMapper();


    public NotificationAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.settings = appSettings.getNotificationRetrievalAPI();
        this.appSettings = appSettings;
        this.token = token;

    }

    /**
     * Returns ClientResponse<NotificationResponse>
     */
    public ClientResponse<NotificationResponse> getNotifications() {
        DataFileWriterUtils fileWriter = new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

        try {


            // -----------------------------
            // Query parameters
            // -----------------------------
            Map<String, String> queryParams = new HashMap<>();
            settings.getRequestParameters().forEach(param -> {
                if (param.isInject() && param.getName() != null && param.getValue() != null) {
                    queryParams.put(param.getName(), param.getValue());
                }
            });

            String queryString = URLUtils.toQueryString(queryParams);

            // -----------------------------
            // Parse URL
            // -----------------------------
            var parsed = URLUtils.parseUrl(settings.getEndpointURL());
            String resourcePathWithQuery = parsed.getResourcePath() + queryString;
            String finalUrl = parsed.getBaseUrl() + resourcePathWithQuery;

            // -----------------------------
            // Headers
            // -----------------------------
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", settings.getAccept());
            headers.put("Authorization", "Bearer " + token);

            // -----------------------------
            // Build cURL
            // -----------------------------
            String curlCommand = CurlUtil.buildGetCurl(finalUrl, headers);
            log.info("Outgoing cURL: {}", curlCommand);
            fileWriter.writeText("notification-retrieval-java", "curl", curlCommand);

            // -----------------------------
            // HTTP request using try-with-resources
            // -----------------------------

            var timeoutMs = (int) settings.getHttpClientRequestTimeOutSeconds() * 1000;

            HttpGet httpGet = HttpClientUtil.getHttpGet(finalUrl,timeoutMs,settings.getAccept(),token);


            try (CloseableHttpClient client = HttpClientBuilder.create().build();
                 CloseableHttpResponse response = client.execute(httpGet)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Failed processing Notification Retrieval Data! Status: {}", statusCode);

                    fileWriter.writeJson("notification-retrieval-java",
                            "response-failed-" + GuidUtil.generateGuid(),
                            body);

                    return new ClientResponse<>(null, false, body);
                }

                // -----------------------------
                // Success case
                // -----------------------------
                NotificationResponse successResponse = mapper.readValue(body, NotificationResponse.class);

                fileWriter.writeJson("notification-retrieval-java",
                        "response-success-" + GuidUtil.generateGuid(),
                        successResponse);
                return new ClientResponse<>(successResponse, true, null);
            }

        } catch (Exception ex) {
            log.error("Exception during Notification Retrieval: {}", ex.getMessage(), ex);

            return new ClientResponse<>(null, false, ex.getMessage());
        }
    }

}
