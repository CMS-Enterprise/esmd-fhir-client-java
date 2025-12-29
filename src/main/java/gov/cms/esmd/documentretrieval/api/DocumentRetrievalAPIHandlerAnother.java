package gov.cms.esmd.documentretrieval.api;


import gov.cms.esmd.documentretrieval.bean.*;
import gov.cms.esmd.utilities.JSONUtility;
import gov.cms.esmd.utilities.bean.DocumentRetrievalAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DocumentRetrievalAPIHandlerAnother {

    private final HttpClient httpClient;

    public DocumentRetrievalAPIHandlerAnother(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public DocumentRetrievalAPIResponseWrapper retrieveDocument(
            String token, DocumentRetrievalAPI documentRetrievalAPI) throws Exception {
        log.info("Request: {}", JSONUtility.toJson(documentRetrievalAPI));

        // Build query params
        Map<String, String> queryParams = new HashMap<>();
        if (documentRetrievalAPI.getRequestParameters() != null) {
            for (DocumentRetrievalAPI.RequestParameter param : documentRetrievalAPI.getRequestParameters()) {
                if (Boolean.TRUE.equals(param.getInject())) {
                    queryParams.put(param.getName(), param.getValue());
                }
            }
        }

        String urlWithParams = documentRetrievalAPI.getEndpointURL();
        if (!queryParams.isEmpty()) {
            String queryString = queryParams.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));
            urlWithParams += "?" + queryString;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .header("Authorization", "Bearer " + token)
                .header("Accept", documentRetrievalAPI.getAccept())
                .timeout(Duration.ofSeconds((long) documentRetrievalAPI.getHttpClientRequestTimeOutSeconds()))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonContent = response.body();

            if (response.statusCode() >= 400) {
                log.error("Http Response Code: {}", response.statusCode());
                log.error("Http Response Content: {}", jsonContent);
                DocumentRetrievalAPIFailedResponse failedResponse =
                        JSONUtility.fromJson(jsonContent, DocumentRetrievalAPIFailedResponse.class);
                return new DocumentRetrievalAPIResponseWrapper(failedResponse, null);
            } else {
                log.info("Http Response Code: {}", response.statusCode());
                log.info("Http Response Content: {}", jsonContent);
                DocumentRetrievalAPISuccessResponse successResponse =
                        JSONUtility.fromJson(jsonContent, DocumentRetrievalAPISuccessResponse.class);
                return new DocumentRetrievalAPIResponseWrapper(null, successResponse);
            }
        } catch (Exception ex) {
            log.error("Exception during document retrieval: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Data
    public static class DocumentRetrievalAPIResponseWrapper {
        private final DocumentRetrievalAPIFailedResponse failedResponse;
        private final DocumentRetrievalAPISuccessResponse successResponse;
    }
}
