package gov.cms.esmd.documentretrieval.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinaryClientResponse {

    private String resourceType;
    private String id;
    private Meta meta;
    private List<Issue> issue;
    private String contentType;
    private String data;

    // ---------------- Meta ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private List<String> profile;
        private List<Security> security;
    }

    // ---------------- Security ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Security {
        private List<Coding> coding;
    }

    // ---------------- Coding ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coding {
        private String system;
        private String code;
        private String display;
    }

    // ---------------- Issue ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Issue {
        private String severity;
        private String code;
        private Details details;
    }

    // ---------------- Details ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Details {
        private List<Coding> coding;
    }
}
