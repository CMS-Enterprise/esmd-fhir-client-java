package gov.cms.esmd.documentretrieval.api;


import gov.cms.esmd.documentretrieval.bean.DocumentRetrievalAPISuccessResponse;
import gov.cms.esmd.utilities.JSONUtility;
import gov.cms.esmd.utilities.bean.DocumentRetrievalAPI;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;

public class DocumentRetrievalAPIHandlerAnotherTest {

    private static final Logger logger = LoggerFactory.getLogger(DocumentRetrievalAPIHandlerAnotherTest.class);

    @Test
    public void testRetrieveDocument_ReturnsSuccessResponse() throws Exception {
        // Sample JSON response
        String responseStr = "{\n" +
                "  \"resourceType\": \"Bundle\",\n" +
                "  \"id\": \"3f187b67-468e-4227-b2d8-1177dec8df78\",\n" +
                "  \"type\": \"searchset\",\n" +
                "  \"total\": 1,\n" +
                "  \"entry\": [\n" +
                "    {\n" +
                "      \"fullUrl\": \"https://terminology.esmduat.cms.gov:8099/fhir/DocumentReference/TXE0007232564EC-LETTERS\",\n" +
                "      \"resource\": {\n" +
                "        \"resourceType\": \"DocumentReference\",\n" +
                "        \"id\": \"TXE0007232564EC-LETTERS\",\n" +
                "        \"status\": \"current\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Deserialize expected response
        DocumentRetrievalAPISuccessResponse expectedResponse =
                JSONUtility.fromJson(responseStr, DocumentRetrievalAPISuccessResponse.class);

        // Mock HttpClient and HttpResponse
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(responseStr);
        Mockito.when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Create handler
        DocumentRetrievalAPIHandlerAnother handler = new DocumentRetrievalAPIHandlerAnother(mockHttpClient);

        // Setup DocumentRetrievalAPI request
        DocumentRetrievalAPI api = new DocumentRetrievalAPI();
        api.setEndpointURL("https://dev.cpiapigateway.cms.gov/api/esmdf/v1/fhir/DocumentReference");
        api.setAccept("application/json");
        api.setHttpClientRequestTimeOutSeconds(2.0);
        api.setRequestParameters(Collections.emptyList());

        // Act
        DocumentRetrievalAPIHandlerAnother.DocumentRetrievalAPIResponseWrapper wrapper =
                handler.retrieveDocument("eyJraWQiOiJQbUJ...", api);

        DocumentRetrievalAPISuccessResponse res = wrapper.getSuccessResponse();

        // Assert
        assertNotNull(res, "Success response should not be null");
        assertEquals("DocumentReference", res.getEntry().get(0).getResource().getResourceType());
        assertNotNull(res.getEntry().get(0).getResource().getId());
    }
}
