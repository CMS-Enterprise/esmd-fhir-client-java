package gov.cms.esmd.documentretrieval.api;

import gov.cms.esmd.documentretrieval.bean.BinaryAPIFailedResponse;
import gov.cms.esmd.documentretrieval.bean.BinaryAPISuccessResponse;
import gov.cms.esmd.utilities.JSONUtility;
import gov.cms.esmd.utilities.bean.BinaryAPI;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class BinaryAPIHandlerTest {

    @Test
    public void testGetBinaryFileData_ReturnsSuccess() throws Exception {

        // Mock JSON success response
        String jsonResponse = "{ \"fileName\": \"ABC123.pdf\", \"status\": \"READY\" }";

        BinaryAPISuccessResponse expected =
                JSONUtility.fromJson(jsonResponse, BinaryAPISuccessResponse.class);

        // Mock HTTP components
        CloseableHttpClient mockClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = Mockito.mock(CloseableHttpResponse.class);
        StatusLine mockStatusLine = Mockito.mock(StatusLine.class);
        HttpEntity mockEntity = new StringEntity(jsonResponse);

        Mockito.when(mockClient.execute(any())).thenReturn(mockResponse);
        Mockito.when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(200);
        Mockito.when(mockResponse.getEntity()).thenReturn(mockEntity);

        // API info
        BinaryAPI api = new BinaryAPI();
        api.setEndpointURL("https://dev.api/binary/{id}");
        api.setAccept("application/json");

        BinaryAPIHandler handler = new BinaryAPIHandler(mockClient);

        // Act
        BinaryAPIHandler.BinaryAPIResult result =
                handler.getBinaryFileData("token123", "ABC123", api);

        // Assert
        assertTrue(result.isSuccess());
        BinaryAPISuccessResponse response = result.getSuccess();

        assertEquals("Binary", response.getResourceType());
        assertEquals("12345", response.getId());
        assertEquals("application/pdf", response.getContentType());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetBinaryFileData_ReturnsFailureResponse() throws Exception {

        // ---- sample failure JSON ----
        String failureJson = "{\n" +
                "  \"resourceType\": \"OperationOutcome\",\n" +
                "  \"meta\": { \"versionId\": \"1\", \"lastUpdated\": \"2023-10-01T10:00:00Z\" },\n" +
                "  \"issue\": [\n" +
                "    {\n" +
                "      \"severity\": \"error\",\n" +
                "      \"code\": \"invalid\",\n" +
                "      \"diagnostics\": \"Invalid ID format\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // ---- mock the HTTP response ----
        CloseableHttpClient mockClient = Mockito.mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = Mockito.mock(CloseableHttpResponse.class);
        StatusLine mockStatusLine = Mockito.mock(StatusLine.class);
        HttpEntity mockEntity = new StringEntity(failureJson);

        Mockito.when(mockStatusLine.getStatusCode()).thenReturn(400);
        Mockito.when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        Mockito.when(mockResponse.getEntity()).thenReturn(mockEntity);
        Mockito.when(mockClient.execute(any(HttpGet.class))).thenReturn(mockResponse);

        // ---- API metadata ----
        BinaryAPI api = new BinaryAPI();
        api.setEndpointURL("https://example.com/binary/{id}");
        api.setAccept("application/json");

        // ---- instantiate handler ----
        BinaryAPIHandler handler = new BinaryAPIHandler(mockClient);

        // ---- execute ----
        BinaryAPIHandler.BinaryAPIResult result = handler.getBinaryFileData("token123", "BAD-ID", api);

        // ---- assertions ----
        assertTrue(result.isFailure(), "Result should be a failure");
        assertNull(result.getSuccess(), "Success object must be null");

        BinaryAPIFailedResponse failure = result.getFailure();
        assertNotNull(failure, "Failure response must not be null");

        // Validate fields
        assertEquals("OperationOutcome", failure.getResourceType());
        assertNotNull(failure.getMeta());
       // assertEquals("1", failure.getMeta().getVersionId());

        assertNotNull(failure.getIssue());
        assertEquals(1, failure.getIssue().size());
        assertEquals("error", failure.getIssue().get(0).getSeverity());
        assertEquals("invalid", failure.getIssue().get(0).getCode());
        assertEquals("Invalid ID format", failure.getIssue().get(0).getDiagnostics());
    }

    @Test
    public void testGetBinaryFileData_ThrowsIOException() throws Exception {

        CloseableHttpClient mockClient = Mockito.mock(CloseableHttpClient.class);

        // Simulate IOException when executing request
        Mockito.when(mockClient.execute(any())).thenThrow(new IOException("Network error"));

        BinaryAPI binaryAPI = new BinaryAPI();
        binaryAPI.setEndpointURL("https://example.com/binary/{id}");
        binaryAPI.setAccept("application/json");

        BinaryAPIHandler handler = new BinaryAPIHandler(mockClient);

        // Assert exception
        assertThrows(IOException.class, () ->
                handler.getBinaryFileData("tokenABC", "ID88", binaryAPI));
    }
}
