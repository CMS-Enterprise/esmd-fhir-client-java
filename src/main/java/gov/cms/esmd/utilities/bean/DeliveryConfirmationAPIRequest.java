package gov.cms.esmd.utilities.bean;

import gov.cms.esmd.documentretrieval.bean.DocumentRetrievalAPISuccessResponse;
import lombok.Data;

import java.util.List;

public class DeliveryConfirmationAPIRequest {
    private String resourceType;
    private String id;
    private DocumentRetrievalAPISuccessResponse.Meta meta;
    private List<Contained> contained;
    private List<DocumentRetrievalAPISuccessResponse.Extension> extension;

    @Data
    public static class Contained {
        private String resourceType;
        private String id;
        private DocumentRetrievalAPISuccessResponse.Meta meta;
        private List<Issue> issue;
    }

    @Data
    public static class Issue {
        private String severity;
        private String code;
        private String diagnostics;
    }
}
