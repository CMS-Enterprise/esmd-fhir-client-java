package gov.cms.esmd.auth.api;


import gov.cms.esmd.auth.bean.AuthInfo;
import gov.cms.esmd.auth.bean.AuthResponse;
import gov.cms.esmd.auth.bean.ErrorResponse;
import gov.cms.esmd.utilities.JSONUtility;
import gov.cms.esmd.utilities.PropertiesUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

public class AuthApiClient implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(AuthApiClient.class);
    private static String AuthApiClientConnectionStatus = "true";

    private final CloseableHttpClient httpClient;
    private final Properties apiProperties;
    private final String authUrl;

    public AuthApiClient(String authUrl) {
        this.apiProperties = PropertiesUtils.loadProperties();
        this.httpClient = HttpClients.createDefault();
        this.authUrl = authUrl;
    }
    public AuthApiClient(String authUrl, CloseableHttpClient httpClient) {
        this.apiProperties = PropertiesUtils.loadProperties();
        this.httpClient = httpClient;
        this.authUrl = authUrl;
    }
    public AuthResponse getToken(AuthInfo authInfo, String scope, String mailbox) {
        log.info("getToken() >>");
        try {
            log.info("Mail Routing ID {}",  mailbox);
            HttpPost request = new HttpPost(authUrl);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("clientid", authInfo.getClientkey());
            request.addHeader("clientsecret", authInfo.getClientsecret());
            request.addHeader("username", authInfo.getUsername());
            request.addHeader("passwd", base64Encode(authInfo.getPassword()));
            request.addHeader("scope", scope);
            request.addHeader("mailboxid", mailbox);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new IOException("Response entity is null");
            }
            System.out.println("Entity type: " + entity.getClass());
            System.out.println("Content length: " + entity.getContentLength());
            String responseContent = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            System.out.println("Response content: " + responseContent);

            if (response.getStatusLine().getStatusCode() != 200) {
                log.error("Request failed with status code {}. Error message: {}",
                        response.getStatusLine().getStatusCode(), responseContent);
                ErrorResponse errorResponse = retrieveErrorFromResponseContent(responseContent);
                AuthResponse ar = new AuthResponse();
                ar.setStatusCode("Failed");
                ar.setError("Request failed with status code " + response.getStatusLine().getStatusCode()
                        + ". Error message: " + (errorResponse != null
                        ? errorResponse.getErrorDescription() : "Unknown error"));
                return ar;
            }

            AuthResponse authResponse = JSONUtility.deserialize(responseContent, AuthResponse.class);
            if (authResponse.getAccess_token() == null || authResponse.getError() != null
                    || "401".equals(authResponse.getStatusCode()) || authResponse.getMessage() != null
                    || authResponse.getResult() != null) {
                handleAuthResponseError(authResponse);
            }

            authResponse.setAccess_token("Bearer " + authResponse.getAccess_token());
            log.info("getToken() <<");
            return authResponse;
        } catch (Exception e) {
            log.error("An error occurred while getting the token: " + e.getMessage(), e);
            throw new IllegalStateException("An error occurred while getting the token.", e);
        }
    }

    private void handleAuthResponseError(AuthResponse authResponse) throws AuthenticationException {
        AuthApiClientConnectionStatus = "false";
        String errorMessage = authResponse.getError() != null ? authResponse.getError()
                : authResponse.getMessage() != null
                ? authResponse.getMessage() : authResponse.getResult();
        throw new AuthenticationException(errorMessage);
    }

    public String base64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private ErrorResponse retrieveErrorFromResponseContent(String responseContent) {
        try {
            return JSONUtility.deserialize(responseContent, ErrorResponse.class);
        } catch (Exception e) {
            log.error("An error occurred while parsing the error response: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }
}

