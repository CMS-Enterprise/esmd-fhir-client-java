package gov.cms.esmd.bundlesubmission.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gov.cms.esmd.bundlesubmission.model.BundlePractitionerResponse;
import gov.cms.esmd.common.model.ClientResponse;
import gov.cms.esmd.common.util.CurlUtil;
import gov.cms.esmd.common.util.DataFileWriterUtils;
import gov.cms.esmd.common.util.GuidUtil;
import gov.cms.esmd.config.model.AppConfigJackson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static gov.cms.esmd.common.util.HttpClientUtil.getHttpPost;

@Slf4j
public class BundlePractitionerAPIHandler {

    private final AppConfigJackson.AppSettings.BundlePractitionerAPI bundlePractitionerApi;
    private final AppConfigJackson.AppSettings appSettings;
    private final String token;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public BundlePractitionerAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.appSettings = appSettings;
        this.bundlePractitionerApi = appSettings.getBundlePractitionerAPI();
        this.token = token;
    }

    /**
     * Returns either:
     * - BundlePractitionerResponse (success)
     * - CommonFailedResponse      (API failure)
     */
    public ClientResponse<BundlePractitionerResponse> processBundlePractitionerRequest(String guid) throws Exception {
        DataFileWriterUtils fileWriter = new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

        try {

            String endpointUrl = bundlePractitionerApi.getEndpointURL();
            int timeoutMs = (int) bundlePractitionerApi.getHttpClientRequestTimeOutSeconds() * 1000;


            // ---------------------- Headers -----------------------
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", bundlePractitionerApi.getContentType());
            headers.put("Accept", bundlePractitionerApi.getAccept());
            headers.put("Authorization", "Bearer " + token);

            var request = bundlePractitionerApi.getRequest();
            String id = (guid != null ? guid : UUID.randomUUID().toString());

            // Replace ${PractitionerIdValue}
            String jsonUpdated = mapper.writeValueAsString(request)
                    .replace("${PractitionerIdValue}", id);


            fileWriter.writeJson("bundle-practitioner-java",
                    "request-" + GuidUtil.generateGuid(),
                    jsonUpdated);


            // ---------------------- Build POST ---------------------

            // Build http post request
            HttpPost httpPost = getHttpPost(endpointUrl, timeoutMs, headers, jsonUpdated);

            log.info("Token request (POST): {}", endpointUrl);


            // ---------------------- Build cURL ---------------------
            String curlCommand = CurlUtil.toCurl(endpointUrl, null, "POST", headers, jsonUpdated, null);
            log.info("Outgoing cURL: {}", curlCommand);
            fileWriter.writeText("bundle-practitioner-java", "curl", curlCommand);


            // ---------------------- Execute ------------------------
            // Execute request
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Failed processing bundle practitioner request! Status: {}", statusCode);

                    fileWriter.writeJson("bundle-practitioner-java",
                            "response-failed-" + GuidUtil.generateGuid(),
                            body);

                    return new ClientResponse<>(null, false, body);
                }

                // -----------------------------
                // Success case
                // -----------------------------
                BundlePractitionerResponse successResponse = mapper.readValue(body, BundlePractitionerResponse.class);

                fileWriter.writeJson("bundle-practitioner-java",
                        "response-success-" + GuidUtil.generateGuid(),
                        successResponse);
                return new ClientResponse<>(successResponse, true, null);

            }

        } catch (Exception e) {
            log.error("Exception while processing bundle processing Request: {}", e.getMessage());
            return new ClientResponse<>(null, false, e.getMessage() == null ? e.getCause().getMessage() : e.getMessage());
        }

    }


}
