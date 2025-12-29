package gov.cms.esmd.documentretrieval.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gov.cms.esmd.common.model.ClientResponse;
import gov.cms.esmd.common.util.CurlUtil;
import gov.cms.esmd.common.util.DataFileWriterUtils;
import gov.cms.esmd.common.util.GuidUtil;
import gov.cms.esmd.config.model.AppConfigJackson;

import gov.cms.esmd.documentretrieval.model.DeliveryConfirmationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static gov.cms.esmd.common.util.HttpClientUtil.getHttpPost;

@Slf4j
public class DeliveryConfirmationAPIHandler {

    private final AppConfigJackson.AppSettings appSettings;
    private final AppConfigJackson.AppSettings.DeliveryConfirmationAPI deliveryConfirmationAPISettings;
    private final String token;

    private final ObjectMapper mapper =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public DeliveryConfirmationAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.appSettings = appSettings;
        this.deliveryConfirmationAPISettings = appSettings.getDeliveryConfirmationAPI();
        this.token = token;
    }

    /**
     *
     * processDeliveryConfirmation()
     */
    public ClientResponse<DeliveryConfirmationResponse> processDeliveryConfirmation() {
        DataFileWriterUtils fileWriter =
                new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

        try {
            String endpointUrl = deliveryConfirmationAPISettings.getEndpointURL();
            int timeoutMs =
                    (int) deliveryConfirmationAPISettings.getHttpClientRequestTimeOutSeconds() * 1000;

            // ---------------------- Headers -----------------------
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", deliveryConfirmationAPISettings.getContentType());
            headers.put("Accept", deliveryConfirmationAPISettings.getAccept());
            headers.put("Authorization", "Bearer " + token);

            Object request = deliveryConfirmationAPISettings.getRequest();

            String requestJson = mapper.writeValueAsString(request);

            // ---------------------- Write request -----------------
            fileWriter.writeJson(
                    "delivery-confirmation-java",
                    "request-" + GuidUtil.generateGuid(),
                    requestJson
            );

            // ---------------------- Build POST --------------------
            HttpPost httpPost = getHttpPost(endpointUrl, timeoutMs, headers, requestJson);

            log.info("DeliveryConfirmation request (POST): {}", endpointUrl);

            // ---------------------- cURL --------------------------
            String curlCommand =
                    CurlUtil.toCurl(endpointUrl, null, "POST", headers, requestJson, null);

            log.info("Outgoing cURL: {}", curlCommand);
            fileWriter.writeText("delivery-confirmation-java", "curl", curlCommand);

            // ---------------------- Execute -----------------------
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Failed processing DeliveryConfirmation API! Status: {}", statusCode);

                    fileWriter.writeJson(
                            "delivery-confirmation-java",
                            "response-failed-" + GuidUtil.generateGuid(),
                            body
                    );

                    return new ClientResponse<>(null, false, body);
                }

                // ---------------------- Success ---------------------
                DeliveryConfirmationResponse successResponse =
                        mapper.readValue(body, DeliveryConfirmationResponse.class);

                fileWriter.writeJson(
                        "delivery-confirmation-java",
                        "response-success-" + GuidUtil.generateGuid(),
                        successResponse
                );

                return new ClientResponse<>(successResponse, true, null);
            }

        } catch (Exception e) {
            log.error("Exception while processing DeliveryConfirmation API request", e);
            return new ClientResponse<>(
                    null,
                    false,
                    e.getMessage() != null ? e.getMessage() : e.getCause().getMessage()
            );
        }
    }
}
