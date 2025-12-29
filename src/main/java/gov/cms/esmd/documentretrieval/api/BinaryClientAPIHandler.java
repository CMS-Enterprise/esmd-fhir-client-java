package gov.cms.esmd.documentretrieval.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import gov.cms.esmd.common.model.ClientResponse;

import gov.cms.esmd.common.util.CurlUtil;
import gov.cms.esmd.common.util.DataFileWriterUtils;
import gov.cms.esmd.common.util.GuidUtil;

import gov.cms.esmd.common.util.HttpClientUtil;
import gov.cms.esmd.config.model.AppConfigJackson;
import gov.cms.esmd.documentretrieval.model.BinaryClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BinaryClientAPIHandler {

    private final AppConfigJackson.AppSettings appSettings;
    private final AppConfigJackson.AppSettings.BinaryAPI binaryClientAPISettings;
    private final String token;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public BinaryClientAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.appSettings = appSettings;
        this.binaryClientAPISettings = appSettings.getBinaryAPI();
        this.token = token;
    }

    /**
     * Fetches binary file data from the Binary API.
     */
    public ClientResponse<BinaryClientResponse> getBinaryFileData(String fileNameIDValue) {
        try {
            DataFileWriterUtils fileWriter = new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

            String endpointUrl = binaryClientAPISettings.getEndpointURL();
            if (fileNameIDValue == null || fileNameIDValue.isEmpty()) {
                fileNameIDValue = binaryClientAPISettings.getFileNameId();
            }
            endpointUrl = endpointUrl.replace("{id}", fileNameIDValue);

            int timeoutMs = (int) binaryClientAPISettings.getHttpClientRequestTimeOutSeconds() * 1000;

            // ---------------------- Headers -----------------------
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", binaryClientAPISettings.getAccept());
            headers.put("Authorization", "Bearer " + token);

            // Build HTTP GET request
            HttpGet httpGet = HttpClientUtil.getHttpGet(endpointUrl,timeoutMs,binaryClientAPISettings.getAccept(),token);

            log.info("Token request (GET): {}", endpointUrl);

            // Build cURL command
            // -----------------------------
            // Build cURL
            // -----------------------------
            String curlCommand = CurlUtil.buildGetCurl(endpointUrl, headers);
            log.info("Outgoing cURL: {}", curlCommand);
            fileWriter.writeText("binary-client-java", "curl", curlCommand);

            // Execute request
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpGet)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Failed processing Binary API request! Status: {}", statusCode);

                    fileWriter.writeJson("binary-client-java",
                            "response-failed-" + GuidUtil.generateGuid(),
                            body);

                    return new ClientResponse<>(null, false, body);
                }

                // Success case
                BinaryClientResponse successResponse = mapper.readValue(body, BinaryClientResponse.class);

                fileWriter.writeJson("binary-client-java",
                        "response-success-" + GuidUtil.generateGuid(),
                        successResponse);

                return new ClientResponse<>(successResponse, true, null);
            }

        } catch (Exception e) {
            log.error("Exception while processing Binary API Request: {}", e.getMessage());
            return new ClientResponse<>(null, false, e.getMessage() == null ? e.getCause().getMessage() : e.getMessage());
        }
    }
}
