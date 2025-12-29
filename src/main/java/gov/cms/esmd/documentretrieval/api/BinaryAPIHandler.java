package gov.cms.esmd.documentretrieval.api;

import gov.cms.esmd.documentretrieval.bean.BinaryAPIFailedResponse;
import gov.cms.esmd.documentretrieval.bean.BinaryAPISuccessResponse;
import gov.cms.esmd.utilities.JSONUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import gov.cms.esmd.utilities.bean.BinaryAPI;

@Slf4j
public class BinaryAPIHandler {

    private final CloseableHttpClient httpClient;

    public BinaryAPIHandler(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public BinaryAPIResult getBinaryFileData(String token, String fileNameId, BinaryAPI binaryAPI) throws IOException {
        try {
            String url = binaryAPI.getEndpointURL().replace("{id}", fileNameId);
            URI uri = new URIBuilder(url).build();
            HttpGet request = new HttpGet(uri);

            // Set headers
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Accept", binaryAPI.getAccept());

            log.info("Request: {}", JSONUtility.toJson(binaryAPI));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonContent = EntityUtils.toString(response.getEntity());

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Http Response Code: {}", statusCode);
                    log.error("Http Response Content: {}", jsonContent);
                    BinaryAPIFailedResponse failedResponse = JSONUtility.fromJson(
                            jsonContent, BinaryAPIFailedResponse.class);
                    return BinaryAPIResult.failure(failedResponse);
                } else {
                    log.info("Http Response Code: {}", statusCode);
                    log.info("Http Response Content: {}", jsonContent);
                    BinaryAPISuccessResponse successResponse = JSONUtility.fromJson(
                            jsonContent, BinaryAPISuccessResponse.class);
                    return BinaryAPIResult.success(successResponse);
                }
            }
        } catch (IOException ex) {
            log.error("Exception during HTTP call: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected exception: {}", ex.getMessage(), ex);
            throw new IOException(ex);
        }
    }


    @lombok.Value
    public static class BinaryAPIResult {
        BinaryAPISuccessResponse success;
        BinaryAPIFailedResponse failure;

        public static BinaryAPIResult success(BinaryAPISuccessResponse success) {
            return new BinaryAPIResult(success, null);
        }

        public static BinaryAPIResult failure(BinaryAPIFailedResponse failure) {
            return new BinaryAPIResult(null, failure);
        }

        public boolean isSuccess() {
            return success != null;
        }

        public boolean isFailure() {
            return failure != null;
        }
    }
}
