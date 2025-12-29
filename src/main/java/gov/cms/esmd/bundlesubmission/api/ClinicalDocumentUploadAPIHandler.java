package gov.cms.esmd.bundlesubmission.api;


import gov.cms.esmd.bundlesubmission.model.UploadClinicalDocumentResponse;
import gov.cms.esmd.common.model.ClientResponse;
import gov.cms.esmd.common.util.*;
import gov.cms.esmd.config.model.AppConfigJackson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static gov.cms.esmd.common.util.HttpClientUtil.getHttpPost;

@Slf4j
public class ClinicalDocumentUploadAPIHandler {

    private final AppConfigJackson.AppSettings.UploadClinicalDocumentAPI uploadSettings;
    private final AppConfigJackson.AppSettings appSettings;
    private final String token;
    private final ObjectMapper mapper = new ObjectMapper();

    public ClinicalDocumentUploadAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.appSettings = appSettings;
        this.uploadSettings = appSettings.getUploadClinicalDocumentAPI();
        this.token = token;
    }

    /**
     * Uploads the clinical XML document to the upload clinical document.
     */
    public ClientResponse<UploadClinicalDocumentResponse> uploadClinicalDocument(String preSignedURL, String fileName) {

        DataFileWriterUtils fileWriter = new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

        try {
            // Timeout
            double timeoutSeconds = uploadSettings.getHttpClientRequestTimeOutSeconds();
            int timeoutMs = (int) (timeoutSeconds * 1000);


            // --------------------- Read file ---------------------
            String fullPath = FileUtil.getFullFilePath(
                    appSettings.getBaseFileLocationFolder(),
                    fileName
            );

            String xml = Files.readString(Paths.get(fullPath), StandardCharsets.UTF_8);

            // --------------------- Compute MD5 ---------------------
            String contentMD5 = CryptoUtils.computeContentMd5String(fullPath);
            log.info("ContentMD5 Value: {}", contentMD5);

            byte[] md5Bytes = CryptoUtils.convertBase64StringToBytes(contentMD5);
            String base64MD5 = Base64.getEncoder().encodeToString(md5Bytes);

            // ---------------------- Headers -----------------------
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", uploadSettings.getContentType());
            headers.put("Accept", "*/*");
            headers.put("Authorization", "Bearer " + token);
            headers.put("Content-MD5", base64MD5);


            // ---------------------- Build POST ---------------------

            // Build http post request
            HttpPost httpPost = getHttpPost(preSignedURL, timeoutMs, headers, xml);

            log.info("Token request (POST): {}", preSignedURL);


            // ---------------------- Build cURL ---------------------
            String curlCommand = CurlUtil.toCurl(preSignedURL, null, "POST", headers, xml, null);
            log.info("Outgoing cURL: {}", curlCommand);
            fileWriter.writeText("upload-clinical-document-java", "curl", curlCommand);



            // ---------------------- Execute ------------------------
            // Execute request
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Failed processing upload clinical document request! Status: {}", statusCode);

                    fileWriter.writeJson("upload-clinical-document-java",
                            "response-failed-" + GuidUtil.generateGuid(),
                            body);

                    return new ClientResponse<>(null, false, body);
                }

                // -----------------------------
                // Success case
                // -----------------------------
                UploadClinicalDocumentResponse successResponse = mapper.readValue(body, UploadClinicalDocumentResponse.class);

                fileWriter.writeJson("upload-clinical-document-java",
                        "response-success-" + GuidUtil.generateGuid(),
                        successResponse);
                return new ClientResponse<>(successResponse, true, null);
            }

         
        } catch (Exception e) {
            log.error("Exception while processing upload clinical document Request: {}", e.getMessage());
            return new ClientResponse<>(null, false, e.getMessage()== null ? e.getCause().getMessage() : e.getMessage());
        }
    }


}
