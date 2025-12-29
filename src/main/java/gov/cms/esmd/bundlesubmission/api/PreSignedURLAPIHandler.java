package gov.cms.esmd.bundlesubmission.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cms.esmd.bundlesubmission.model.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.cms.esmd.common.util.HttpClientUtil.getHttpPost;

@Slf4j
public class PreSignedURLAPIHandler {
    private final AppConfigJackson.AppSettings.PresignedURLAPI settings;
    private final AppConfigJackson.AppSettings appSettings;
    private final String token;
    private final ObjectMapper mapper = new ObjectMapper();

    public PreSignedURLAPIHandler(AppConfigJackson.AppSettings appSettings, String token) {
        this.appSettings = appSettings;
        this.settings = appSettings.getPresignedURLAPI();
        this.token = token;
    }

    public ClientResponse<PreSignedURLResponse> getPreSignedURLAsync(String guidId) {
        DataFileWriterUtils fileWriter = new DataFileWriterUtils(appSettings.getBaseFileLocationFolder());

        try {

            String contentType = settings.getContentType();
            String accept = settings.getAccept();
            String endpointURL = settings.getEndpointURL();
            int timeoutMs = (int) settings.getHttpClientRequestTimeOutSeconds() * 1000;

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", contentType);
            headers.put("Accept", accept);
            headers.put("Authorization", "Bearer " + this.token);

            AppConfigJackson.AppSettings.PresignedURLAPI.Request request = settings.getRequest();

            // Assign ID
            if (request.getId() == null && guidId == null) {
                request.setId(GuidUtil.generateGuid());
            } else if (guidId != null) {
                request.setId(guidId);
            }

            String fileName = "";
            AppConfigJackson.AppSettings.PresignedURLAPI.Request.Parameter.Part contentMD5Part = null;
            AppConfigJackson.AppSettings.PresignedURLAPI.Request.Parameter.Part fileSizePart = null;

            // Process request parameters
            if (request.getParameter() != null) {
                for (var param : request.getParameter()) {
                    if (param.getPart() != null) {
                        for (var part : param.getPart()) {
                            if (part == null) continue;

                            switch (part.getName()) {
                                case "filename":
                                    fileName = part.getValueString() != null ? part.getValueString() : "";
                                    break;
                                case "content-md5":
                                    contentMD5Part = part;
                                    break;
                                case "filesize":
                                    fileSizePart = part;
                                    break;
                            }
                        }

                        // Compute full file path
                        String fullFileNamePath = FileUtil.getFullFilePath(
                                appSettings.getBaseFileLocationFolder(),
                                fileName
                        );

                        // Compute MD5
                        if (contentMD5Part != null && contentMD5Part.getValueString().isEmpty()) {
                            String md5Value = CryptoUtils.computeContentMd5String(fullFileNamePath);
                            log.info("ContentMD5 Value: {} ", md5Value);
                            contentMD5Part.setValueString(md5Value);
                        }

                        // Compute file size
                        if (fileSizePart != null && fileSizePart.getValueString().isEmpty()) {
                            fileSizePart.setValueString(
                                    String.valueOf(FileUtil.getFileSizeInMB(fullFileNamePath))
                            );
                        }
                    }
                }
            }


            // -----------------------------
            // Build cURL
            // -----------------------------

            String curlCommand = CurlUtil.toCurl(endpointURL, null, "POST", headers, null, null);
            log.info("Outgoing cURL: {}", curlCommand);
            fileWriter.writeText("preSigned-url-java", "curl", curlCommand);

            fileWriter.writeJson("preSigned-url-java",
                    "request-" + GuidUtil.generateGuid(),
                    JsonUtil.toJson(request));


            // Build http post request
            HttpPost httpPost = getHttpPost(endpointURL, timeoutMs, headers, JsonUtil.toObjectNode(request));

            log.info("Token request (POST): {}", endpointURL);

            // Execute request
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

                int statusCode = response.getStatusLine().getStatusCode();
                String body = response.getEntity() != null
                        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                        : null;

                if (statusCode < 200 || statusCode >= 300) {
                    log.error("Failed processing preSigned URL request! Status: {}", statusCode);

                    fileWriter.writeJson("preSigned-url-java",
                            "response-failed-" + GuidUtil.generateGuid(),
                            body);

                    return new ClientResponse<>(null, false, body);
                }

                // -----------------------------
                // Success case
                // -----------------------------
                PreSignedURLResponse successResponse = mapper.readValue(body, PreSignedURLResponse.class);

                fileWriter.writeJson("preSigned-url-java",
                        "response-success-" + GuidUtil.generateGuid(),
                        successResponse);
                return new ClientResponse<>(successResponse, true, null);
            }

        } catch (Exception e) {
            log.error("Exception while processing preSigned URL Request: {}", e.getMessage());
            return new ClientResponse<>(null, false, e.getMessage());
        }
    }

    /**
     * -------------------------------
     * processPreSignedURLResponse()
     * --------------------------------
     */
    public List<PreSignedURLInfo> processPreSignedURLResponse(
            PreSignedURLResponse preSignedURLAPIResponse) {


        List<PreSignedURLInfo> resultList = new ArrayList<>();

        if (preSignedURLAPIResponse.getParameter() != null) {
            for (Parameter parameter : preSignedURLAPIResponse.getParameter()) {
                if (parameter.getPart() != null) {
                    PreSignedURLInfo info = new PreSignedURLInfo();
                    resultList.add(info);

                    for (Part part : parameter.getPart()) {
                        if (part.getPart() != null) {
                            for (Part2 partItem : part.getPart()) {

                                if (partItem.getValueString() != null) {
                                    info.setPartValueString(
                                            new PartValueString(
                                                    partItem.getName(),
                                                    partItem.getValueString()
                                            )
                                    );
                                }

                                if (partItem.getValueUrl() != null) {
                                    info.setPartValueUrl(
                                            new PartValueUrl(
                                                    partItem.getName(),
                                                    partItem.getValueUrl()
                                            )
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }

        log.info("PreSigned URL Info List: {}", JsonUtil.toJson(resultList));
        return resultList;

    }
}