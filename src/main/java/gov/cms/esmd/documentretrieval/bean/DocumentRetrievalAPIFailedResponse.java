package gov.cms.esmd.documentretrieval.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentRetrievalAPIFailedResponse {

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("issue")
    private List<Issue> issue;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Issue {

        @JsonProperty("extension")
        private List<Extension> extension;

        @JsonProperty("severity")
        private String severity;

        @JsonProperty("code")
        private String code;

        @JsonProperty("details")
        private Details details;

        @JsonProperty("diagnostics")
        private String diagnostics;

        @JsonProperty("location")
        private List<String> location;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Details {

        @JsonProperty("coding")
        private List<Coding> coding;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {

        @JsonProperty("profile")
        private List<String> profile;

        @JsonProperty("security")
        private List<Security> security;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Coding {

        @JsonProperty("system")
        private String system;

        @JsonProperty("code")
        private String code;

        @JsonProperty("display")
        private String display;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Security {

        @JsonProperty("system")
        private String system;

        @JsonProperty("code")
        private String code;

        @JsonProperty("display")
        private String display;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Extension {

        @JsonProperty("url")
        private String url;

        @JsonProperty("valueString")
        private String valueString;

        @JsonProperty("valueCode")
        private String valueCode;
    }
}
