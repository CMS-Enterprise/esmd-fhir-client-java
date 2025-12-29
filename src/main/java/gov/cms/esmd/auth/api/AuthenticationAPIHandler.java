package gov.cms.esmd.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cms.esmd.auth.model.Token;
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
import java.util.*;

import static gov.cms.esmd.common.util.Constants.*;
import static gov.cms.esmd.common.util.HttpClientUtil.getHttpPost;

@Slf4j
public class AuthenticationAPIHandler {


    private final AppConfigJackson.AppSettings appSettings;
    private final AppConfigJackson.AppSettings.AuthenticationAPI authenticationAPISettings;
    private Token cachedToken;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public AuthenticationAPIHandler(AppConfigJackson.AppSettings appSettings) {
        this.appSettings = appSettings;
        this.authenticationAPISettings = appSettings.getAuthenticationAPI();
    }

    public ClientResponse<Token> getToken() {

        DataFileWriterUtils fileWriter = new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

        try {
            // Return cached token if valid
            if (isTokenValid(cachedToken)) {
                return new ClientResponse<>(cachedToken, true, null);
            }

            String endpointUrl = authenticationAPISettings.getEndpointURL();
            int timeoutMs = (int) authenticationAPISettings.getHttpClientRequestTimeOutSeconds() * 1000;


            Map<String, String> headerMap = Map.of(
                    CLIENT_ID, authenticationAPISettings.getClientId(),
                    CLIENT_SECRET, authenticationAPISettings.getClientSecret(),
                    SCOPE, authenticationAPISettings.getScope()
            );


            // Build http post request
            HttpPost httpPost = getHttpPost(endpointUrl, timeoutMs, headerMap);

            log.info("Token request (POST): {}", endpointUrl);

            // Log curl

            String curlCommand = CurlUtil.toCurl(endpointUrl, null, "POST", headerMap, null, null);
            log.info("Outgoing cURL: {}", curlCommand);
            fileWriter.writeText("authentication-java", "curl", curlCommand);

            // Execute request
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

                String responseBody = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                boolean success = response.getStatusLine().getStatusCode() < 400 && responseBody != null;

                // Write response file
                String filePrefix = success ? "response-success-" : "response-failed-";
                fileWriter.writeJsonFile(
                        appSettings.getBaseFileLocationFolder(),
                        "authentication-java",
                        filePrefix + GuidUtil.generateGuid(),
                        responseBody
                );

                if (!success) {
                    log.error("Failed to retrieve token.");
                    return new ClientResponse<>(null, false, responseBody);
                }

                // Deserialize and cache
                Token tokenData = objectMapper.readValue(responseBody, Token.class);
                tokenData.setIssuedAt(new Date());
                cachedToken = tokenData;

                log.info("Token retrieved and cached.");
                return new ClientResponse<>(cachedToken, true, null);
            }

        } catch (Exception e) {
            log.error("Exception while retrieving token: {}", e.getMessage());
            return new ClientResponse<>(null, false, e.getMessage());
        }
    }


    private boolean isTokenValid(Token token) {
        if (token == null || token.getIssuedAt() == null || token.getExpiresIn() == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        long expiresAt = token.getIssuedAt().getTime() + token.getExpiresIn() * 1000;
        long bufferMs = 60 * 1000; // 1 minute buffer
        return now < (expiresAt - bufferMs);
    }


}