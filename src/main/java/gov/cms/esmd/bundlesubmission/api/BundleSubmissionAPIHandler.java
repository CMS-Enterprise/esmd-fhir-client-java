package gov.cms.esmd.bundlesubmission.api;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gov.cms.esmd.bundlesubmission.model.BundleSubmissionResponse;
import gov.cms.esmd.bundlesubmission.model.UploadClinicalDocumentResponse;
import gov.cms.esmd.common.model.ClientResponse;
import gov.cms.esmd.common.util.*;
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
public class BundleSubmissionAPIHandler {

    private final AppConfigJackson.AppSettings.BundleSubmissionAPI bundleSubmissionAPI;
    private final AppConfigJackson.AppSettings appSettings;
    private final String token;

    private final ObjectMapper mapper =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public BundleSubmissionAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.appSettings = appSettings;
        this.bundleSubmissionAPI = appSettings.getBundleSubmissionAPI();
        this.token = token;
    }

    // =====================================================
    // processBundleSubmissionRequest
    // =====================================================
    public ClientResponse<BundleSubmissionResponse> processBundleSubmissionRequest( UploadClinicalDocumentResponse uploadResponse, String sharedGuidId) {

        DataFileWriterUtils fileWriter =
                new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

        try {
            prepareBundleSubmissionRequest(uploadResponse,sharedGuidId);

            String endpointUrl = bundleSubmissionAPI.getEndpointURL();
            int timeoutMs =
                    (int) bundleSubmissionAPI.getHttpClientRequestTimeOutSeconds() * 1000;

            // ---------------- Headers ----------------
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", bundleSubmissionAPI.getContentType());
            headers.put("Accept", bundleSubmissionAPI.getAccept());
            headers.put("Authorization", "Bearer " + token);

            var request = bundleSubmissionAPI.getRequest();

            // -------- ID logic (same as TS) --------
            String id = (isNullOrEmpty(request.getId()) && isNullOrEmpty(sharedGuidId))
                    ? UUID.randomUUID().toString()
                    : (!isNullOrEmpty(sharedGuidId) ? sharedGuidId : request.getId());

            request.setId(id);

            // ---------------- Serialize ----------------
            String jsonRequest = mapper.writeValueAsString(request);

            fileWriter.writeJson(
                    "bundle-submission-java",
                    "request-" + id,
                    jsonRequest
            );

            // ---------------- HTTP POST ----------------
            HttpPost httpPost =
                    getHttpPost(endpointUrl, timeoutMs, headers, jsonRequest);

            // ---------------- cURL ----------------
            String curl =
                    CurlUtil.toCurl(endpointUrl, null, "POST", headers, jsonRequest, null);
            log.info("Outgoing cURL: {}", curl);

            fileWriter.writeText(
                    "bundle-submission-java",
                    "curl",
                    curl
            );

            // ---------------- Execute ----------------
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {

                    log.error("Failed processing Bundle Submission! Status: {}", statusCode);

                    fileWriter.writeJson(
                            "bundle-submission-java",
                            "response-failed-" + id,
                            body
                    );

                    return new ClientResponse<>(null, false, body);
                }

                BundleSubmissionResponse success =
                        mapper.readValue(body, BundleSubmissionResponse.class);

                fileWriter.writeJson(
                        "bundle-submission-java",
                        "response-success-" + id,
                        success
                );

                return new ClientResponse<>(success, true, null);
            }

        } catch (Exception e) {
            log.error("Exception while processing Bundle Submission request", e);
            return new ClientResponse<>(
                    null,
                    false,
                    e.getMessage() != null ? e.getMessage() : e.getCause().getMessage()
            );
        }
    }

    // =====================================================
    // prepareBundleSubmissionRequest (TS mirror)
    // =====================================================
    private void prepareBundleSubmissionRequest(
            UploadClinicalDocumentResponse uploadResponse,
            String sharedGuidId
    ) throws Exception {

        var bundleRequest = bundleSubmissionAPI.getRequest();

        bundleRequest.setId(sharedGuidId);

        String timestamp = DateTimeUtil.getCurrentWithOffset();
        bundleRequest.setTimestamp(timestamp);

        String fullFilePath =
                FileUtil.getFullFilePath(
                        appSettings.getBaseFileLocationFolder(),
                        uploadResponse.getFilename()
                );

        String documentReferenceGuid = GuidUtil.generateGuid();
        String listGuid = GuidUtil.generateGuid();

        if (bundleRequest.getEntry() == null) return;

        for (var entry : bundleRequest.getEntry()) {

            if (entry.getResource() == null) continue;

            var resource = entry.getResource();
            resource.setDate(timestamp);

            // ---------------- DocumentReference ----------------
            if ("DocumentReference".equals(resource.getResourceType())) {

                entry.setFullUrl(
                       urnUuidFormattedValue(documentReferenceGuid)
                );

                resource.setId(documentReferenceGuid);

                if (resource.getContent() != null && !resource.getContent().isEmpty()) {
                    var attachment = resource.getContent().get(0).getAttachment();
                    if (attachment != null) {
                        attachment.setId(documentReferenceGuid + "_document");
                        attachment.setTitle(documentReferenceGuid + "_pkpadmin");
                        attachment.setUrl(uploadResponse.getS3uri());
                        attachment.setContentType("application/xml");
                        attachment.setSize(FileUtil.getFileSizeBytes(fullFilePath));
                        attachment.setHash(CryptoUtils.computeSHA256Checksum(fullFilePath));
                        attachment.setCreation(DateTimeUtil.getCurrentUtc());
                    }
                }

                if (resource.getIdentifier() != null) {
                    resource.getIdentifier().forEach(identifier -> {
                        if (identifier.getSystem() != null &&
                                identifier.getSystem().contains("Esmd-Idn-UniqueId")) {
                            identifier.setValue(sharedGuidId);
                        }
                    });
                }
            }

            // ---------------- List ----------------
            else if ("List".equals(resource.getResourceType())) {

                entry.setFullUrl(
                        urnUuidFormattedValue(listGuid)
                );

                resource.setId(listGuid);

                if (resource.getEntry() != null && !resource.getEntry().isEmpty()) {
                    var listEntry = resource.getEntry().get(0);
                    if (listEntry.getItem() != null) {
                        listEntry.getItem().setReference(
                                urnUuidFormattedValue(documentReferenceGuid)
                        );
                    }
                }

                if (resource.getExtension() != null) {
                    resource.getExtension().forEach(ext -> {
                        if (ext.getUrl() != null &&
                                ext.getUrl().contains("Esmd-Ext-UniqueId")) {
                            ext.setValueString(sharedGuidId);
                        }
                    });
                }
            }
        }
    }

    // =====================================================
    // Utils
    // =====================================================
    private boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String urnUuidFormattedValue(String guid) {
        return "urn:uuid:" + guid;
    }



}

