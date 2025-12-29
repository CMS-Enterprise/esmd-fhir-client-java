package gov.cms.esmd.utilities.bean;

import lombok.Data;

import java.util.List;

@Data
public class DocumentRetrievalAPI extends CommonAppSettings {

    private String accept;

    private List<RequestParameter> requestParameters;

    @Data
    public static class RequestParameter {
        private String name;
        private String value;
        private Boolean inject;
    }
}
