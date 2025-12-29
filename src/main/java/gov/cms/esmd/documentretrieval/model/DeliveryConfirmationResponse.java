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
public class DeliveryConfirmationResponse {

    private String resourceType;
    private String id;
    private Meta meta;
    private List<Contained> contained;
    private List<Extension> extension;
    private String status;
    private String mode;
    private String title;
    private String date;
    private List<Entry> entry;

    // ---------------- Meta ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private String versionId;
        private String lastUpdated;
        private String source;
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
        private String system;
        private String code;
        private String display;
    }

    // ---------------- Contained ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contained {
        private String resourceType;
        private String id;
        private Meta2 meta;
        private List<Issue> issue;
    }

    // ---------------- Meta2 ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta2 {
        private List<String> profile;
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
        private String diagnostics;
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

    // ---------------- Extension ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Extension {
        private String url;
        private String valueString;
        private String valueDateTime;
    }

    // ---------------- Entry ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {
        private Item item;
    }

    // ---------------- Item ----------------
    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String reference;
    }
}
