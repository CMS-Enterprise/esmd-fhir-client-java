package gov.cms.esmd;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cms.esmd.auth.api.AuthenticationAPIHandler;
import gov.cms.esmd.bundlesubmission.api.BundlePractitionerAPIHandler;
import gov.cms.esmd.bundlesubmission.api.BundleSubmissionAPIHandler;
import gov.cms.esmd.bundlesubmission.api.ClinicalDocumentUploadAPIHandler;
import gov.cms.esmd.bundlesubmission.api.PreSignedURLAPIHandler;
import gov.cms.esmd.common.util.GuidUtil;
import gov.cms.esmd.config.YamlConfigLoaderJackson;
import gov.cms.esmd.documentretrieval.api.BinaryClientAPIHandler;
import gov.cms.esmd.documentretrieval.api.DeliveryConfirmationAPIHandler;
import gov.cms.esmd.documentretrieval.api.DocumentRetrievalAPIHandler;
import gov.cms.esmd.notification.api.NotificationAPIHandler;
import gov.cms.esmd.practitioner.api.PractitionerAPIHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainConsoleApplication {

    public static void main(String[] args) {
        try {
            var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");
            var config = loader.getConfig();

            // Print some properties to verify
            log.info("HTTP Timeout: {}", config.getAppSettings().getHttpClientRequestTimeOutSeconds());
            log.info("FHIR Server URL: {}", config.getAppSettings().getFhirServerUrl());
            log.info("Authentication Endpoint: {}", config.getAppSettings().getAuthenticationAPI().getEndpointURL());
            log.info("Delivery Confirmation Endpoint: {}", config.getAppSettings().getDeliveryConfirmationAPI().getEndpointURL());
            var commonSharedGuid = GuidUtil.generateGuid();
            log.info("Common Shared GUID {}", commonSharedGuid);


            // Get Token
            log.info("Getting Token....");
            var token = new AuthenticationAPIHandler(config.getAppSettings()).getToken();
            if (token.isSuccess()) {
                log.info("token value is - {}", token.getResponse().getAccessToken());

                log.info("Now Processing Notification request!");
                var notification = new NotificationAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken()).getNotifications();
                if (notification.isSuccess()) {
                    log.info("Notification Retrieved Success - {}", new ObjectMapper().writeValueAsString(notification.getResponse()));
                } else {
                    log.error("Notification Retrieval Failed - {}", notification.getError());
                }

                log.info("Now Processing PreSignedURL request!");
                var preSignedURLHandler = new PreSignedURLAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var preSignedURL = preSignedURLHandler.getPreSignedURLAsync(commonSharedGuid);
                if (preSignedURL.isSuccess()) {
                    log.info("PreSignedURL Retrieved Success - {}", new ObjectMapper().writeValueAsString(preSignedURL.getResponse()));
                    var urlList = preSignedURLHandler.processPreSignedURLResponse(preSignedURL.getResponse());
                    var clinicalDocumentUploadHandler = new ClinicalDocumentUploadAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                    urlList.forEach(url -> {

                        var uploadDocumentResponse = clinicalDocumentUploadHandler.uploadClinicalDocument(url.getPartValueUrl().getValueUrl(), url.getPartValueString().getValueString());
                        try {

                            if (uploadDocumentResponse.isSuccess()) {
                                log.info("upload document response success - {}", new ObjectMapper().writeValueAsString(uploadDocumentResponse.getResponse()));

                                log.info("Now Processing Bundle Submission request!");
                                var bundleSubmissionAPIHandler = new BundleSubmissionAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                                var bundleSubmissionResponse = bundleSubmissionAPIHandler.processBundleSubmissionRequest(uploadDocumentResponse.getResponse(), commonSharedGuid);
                                if (bundleSubmissionResponse.isSuccess()) {
                                    log.info("processing bundleSubmission success - {}", new ObjectMapper().writeValueAsString(bundleSubmissionResponse.getResponse()));
                                } else {
                                    log.error("processing bundleSubmission Failed - {}", bundleSubmissionResponse.getError());
                                }
                            } else {
                                log.error("upload document Failed - {}", uploadDocumentResponse.getError());
                            }
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                    });
                } else {
                    log.error("PreSignedURL Retrieval Failed - {}", preSignedURL.getError());
                }


                log.info("Now Processing Bundle Practitioner request!");
                var bundlePractitionerHandler = new BundlePractitionerAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var bundlePractitioner = bundlePractitionerHandler.processBundlePractitionerRequest(commonSharedGuid);
                if (bundlePractitioner.isSuccess()) {
                    log.info("Successfully process bundlePractitioner  - {}", new ObjectMapper().writeValueAsString(bundlePractitioner.getResponse()));
                } else {
                    log.error("Bundle Practitioner Failed - {}", bundlePractitioner.getError());
                }

                log.info("Now Processing Binary Client request!");
                var binaryClientHandler = new BinaryClientAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var binaryClientResponse = binaryClientHandler.getBinaryFileData(commonSharedGuid);
                if (binaryClientResponse.isSuccess()) {
                    log.info("Successfully processed Binary client Request  - {}", new ObjectMapper().writeValueAsString(binaryClientResponse.getResponse()));
                } else {
                    log.error("Binary Client Request processing Failed - {}", binaryClientResponse.getError());
                }

                log.info("Now Processing Delivery Confirmation request!");
                var deliveryConfirmationHandler = new DeliveryConfirmationAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var deliveryConfirmationResponse = deliveryConfirmationHandler.processDeliveryConfirmation();
                if (deliveryConfirmationResponse.isSuccess()) {
                    log.info("Successfully processed Delivery Confirmation Request  - {}", new ObjectMapper().writeValueAsString(deliveryConfirmationResponse.getResponse()));
                } else {
                    log.error("Delivery Confirmation Request processing Failed - {}", deliveryConfirmationResponse.getError());
                }

                log.info("Now Processing Practitioner request!");
                var practitionerHandler = new PractitionerAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var practitionerResponse = practitionerHandler.processPractitionerRequest(null);
                if (practitionerResponse.isSuccess()) {
                    log.info("Successfully processed practitioner Request  - {}", new ObjectMapper().writeValueAsString(practitionerResponse.getResponse()));
                } else {
                    log.error("practitioner Request processing Failed - {}", practitionerResponse.getError());
                }

                log.info("Now Processing Document Retrieval request!");
                var documentRetrievalHandler = new DocumentRetrievalAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var documentRetrievalHandlerResponse = documentRetrievalHandler.getDocumentRetrievalData();
                if (documentRetrievalHandlerResponse.isSuccess()) {
                    log.info("Successfully processed document retrieval Request  - {}", new ObjectMapper().writeValueAsString(documentRetrievalHandlerResponse.getResponse()));
                } else {
                    log.error("Document Retrieval Request processing Failed - {}", documentRetrievalHandlerResponse.getError());
                }


            }


        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
