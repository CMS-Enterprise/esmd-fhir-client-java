package gov.cms.esmd.practitioner.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gov.cms.esmd.common.model.ClientResponse;
import gov.cms.esmd.common.util.CurlUtil;
import gov.cms.esmd.common.util.DataFileWriterUtils;
import gov.cms.esmd.common.util.GuidUtil;
import gov.cms.esmd.common.util.HttpClientUtil;
import gov.cms.esmd.config.model.AppConfigJackson;
import gov.cms.esmd.practitioner.model.PractitionerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PractitionerAPIHandler {

    private final AppConfigJackson.AppSettings appSettings;
    private final AppConfigJackson.AppSettings.PractitionerAPI practitionerAPISettings;
    private final String token;

    private final ObjectMapper mapper =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public PractitionerAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.appSettings = appSettings;
        this.practitionerAPISettings = appSettings.getPractitionerAPI();
        this.token = token;
    }

    /**
     * Processes Practitioner API PUT request
     */
    public ClientResponse<PractitionerResponse> processPractitionerRequest(String practitionerId) {
        try {
            DataFileWriterUtils fileWriter =
                    new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

            if (practitionerId == null || practitionerId.isEmpty()) {
                practitionerId = practitionerAPISettings.getRequest().getId();
            }

            String endpointUrl = practitionerAPISettings.getEndpointURL()
                    .replace("{id}", practitionerId);

            var request = practitionerAPISettings.getRequest();
            request.setId(practitionerId);

            int timeoutMs =
                    (int) practitionerAPISettings.getHttpClientRequestTimeOutSeconds() * 1000;

            // ---------------------- Headers -----------------------
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", practitionerAPISettings.getContentType());
            headers.put("Accept", practitionerAPISettings.getAccept());
            headers.put("Authorization", "Bearer " + token);


            String requestJson =
                    mapper.writeValueAsString(request);


            log.info("Practitioner API request (PUT): {}", endpointUrl);

            // ---------------------- cURL -----------------------
            String curlCommand =
                    CurlUtil.toCurl(endpointUrl, null, "PUT", headers, requestJson, null);

            log.info("Outgoing cURL: {}", curlCommand);

            fileWriter.writeText("practitioner-java", "curl", curlCommand);
            fileWriter.writeJson(
                    "practitioner-java",
                    "request-" + GuidUtil.generateGuid(),
                    practitionerAPISettings.getRequest()
            );

            // ---------------------- HTTP PUT -----------------------
            HttpPut httpPut = HttpClientUtil.getHttpPut(endpointUrl, timeoutMs, headers, requestJson);

            // ---------------------- Execute -----------------------
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPut)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Failed processing Practitioner API request! Status: {}", statusCode);

                    fileWriter.writeJson(
                            "practitioner-java",
                            "response-failed-" + GuidUtil.generateGuid(),
                            body
                    );

                    return new ClientResponse<>(null, false, body);
                }

                PractitionerResponse successResponse =
                        mapper.readValue(body, PractitionerResponse.class);

                fileWriter.writeJson(
                        "practitioner-java",
                        "response-success-" + GuidUtil.generateGuid(),
                        successResponse
                );

                return new ClientResponse<>(successResponse, true, null);
            }

        } catch (Exception e) {
            log.error("Exception while processing Practitioner API request", e);
            return new ClientResponse<>(
                    null,
                    false,
                    e.getMessage() == null ? e.getCause().getMessage() : e.getMessage()
            );
        }
    }
}

