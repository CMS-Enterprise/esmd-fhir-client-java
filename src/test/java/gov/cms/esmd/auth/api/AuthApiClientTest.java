package gov.cms.esmd.auth.api;

import gov.cms.esmd.auth.bean.AuthInfo;
import gov.cms.esmd.auth.bean.AuthResponse;
import gov.cms.esmd.auth.bean.ErrorResponse;
import gov.cms.esmd.utilities.Constants;
import gov.cms.esmd.utilities.JSONUtility;
import gov.cms.esmd.utilities.PropertiesUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthApiClientTest {

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    @InjectMocks
    private AuthApiClient authApiClient;

    private AuthInfo authInfo;
    private Properties apiProperties;
    private String url;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.apiProperties = PropertiesUtils.loadProperties();
        String baseUrl = PropertiesUtils.constructBaseUrl(apiProperties, "dev");
        String authUrl = apiProperties.getProperty(Constants.AUTHURLKEY);
        url  = baseUrl + authUrl;
        authApiClient = new AuthApiClient(url);

        authInfo = new AuthInfo("esmdeu0", "esmdLabtesting@1023",
                "3fis5elmln49c3fklkfs5v20ml", "1ad4odlhll6jur6qfbaoa71o52rt836b0v8l3jqh5tlvosvmkc48");
    }

    @Test
    void testGetToken_SuccessResponse() throws Exception {
        // Arrange
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        HttpEntity mockEntity = mock(HttpEntity.class);
        StatusLine mockStatusLine = mock(StatusLine.class);

        when(mockStatusLine.getStatusCode()).thenReturn(200);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockHttpClient.execute(any(HttpPost.class), any(HttpContext.class))).thenReturn(mockResponse);

        String jsonResponse = "{\"access_token\":\"abc123\",\"expires_in\":3600,\"token_type\":\"Bearer\"}";

        try (MockedStatic<EntityUtils> mockedEntityUtils = mockStatic(EntityUtils.class);
             MockedStatic<JSONUtility> mockedJsonUtil = mockStatic(JSONUtility.class)) {

            mockedEntityUtils.when(() ->
                    EntityUtils.toString(any(HttpEntity.class), eq(StandardCharsets.UTF_8))
            ).thenReturn(jsonResponse);

            AuthResponse mockResponseObj = new AuthResponse("abc123", 3600, "Bearer", null, null, null, null);
            mockedJsonUtil.when(() -> JSONUtility.deserialize(anyString(), eq(AuthResponse.class)))
                    .thenReturn(mockResponseObj);

            // Create test client that uses mocked httpClient
            AuthApiClient client = new AuthApiClient(url) {
                @Override
                public void close() {} // no-op
            };

            // Act
            AuthResponse result = client.getToken(authInfo, "rc/status", "ESD002");

            // Assert
            assertNotNull(result);
            assertTrue(result.getAccess_token().startsWith("Bearer "));
            assertEquals("Bearer", result.getToken_type());
        }
    }


    @Test
    void testGetToken_ErrorResponse_401() throws Exception {
        // Arrange
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        HttpEntity mockEntity = mock(HttpEntity.class);
        StatusLine mockStatusLine = mock(StatusLine.class);

        when(mockStatusLine.getStatusCode()).thenReturn(200); // 200 but body indicates 401-like content
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockResponse.getEntity()).thenReturn(mockEntity);
        when(mockHttpClient.execute(any(HttpPost.class))).thenReturn(mockResponse);

        String jsonResponse = "{\"error\": \"Unauthorized\", \"statusCode\": \"401\"}";

        try (MockedStatic<EntityUtils> mockedEntityUtils = mockStatic(EntityUtils.class);
             MockedStatic<JSONUtility> mockedJsonUtil = mockStatic(JSONUtility.class)) {

            mockedEntityUtils.when(() ->
                    EntityUtils.toString(eq(mockEntity), eq(StandardCharsets.UTF_8))
            ).thenReturn(jsonResponse);

            AuthResponse authResponse = new AuthResponse(null, 0, null, "Unauthorized", "401", null, null);
            mockedJsonUtil.when(() -> JSONUtility.deserialize(anyString(), eq(AuthResponse.class)))
                    .thenReturn(authResponse);

            // Create test client with mocked httpClient
            AuthApiClient client = new AuthApiClient(url, mockHttpClient) {
                @Override
                public void close() {} // skip close
            };

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> client.getToken(authInfo, "rc/status", "ESD002"));
            assertTrue(ex.getCause() instanceof AuthenticationException);
            assertEquals("Unauthorized", ex.getCause().getMessage());
        }
    }


    @Test
    void testGetToken_HttpErrorResponse() throws Exception {
        String errorJson = "{\"errorDescription\": \"Invalid credentials\"}";

        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(400);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(EntityUtils.toString(httpEntity, StandardCharsets.UTF_8)).thenReturn(errorJson);

        try (MockedStatic<JSONUtility> mockedJsonUtil = mockStatic(JSONUtility.class)) {
            mockedJsonUtil.when(() -> JSONUtility.deserialize(anyString(), eq(ErrorResponse.class)))
                    .thenReturn(new ErrorResponse("E001", "", "Invalid credentials"));

            AuthResponse response = authApiClient.getToken(authInfo, "scopeVal", "mailboxId");

            // Assert
            assertNotNull(response);
            assertEquals("Failed", response.getStatusCode());
            assertTrue(response.getError().contains("Invalid credentials"));
        }
    }

    @Test
    void testBase64Encode() {
        String encoded = Base64.getEncoder().encodeToString("secret".getBytes(StandardCharsets.UTF_8));
        assertEquals(encoded, new AuthApiClient("url").base64Encode("secret"));
    }
}