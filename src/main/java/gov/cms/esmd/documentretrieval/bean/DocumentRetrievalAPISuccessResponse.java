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
public class DocumentRetrievalAPISuccessResponse {

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("id")
    private String id;

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("type")
    private String type;

    @JsonProperty("total")
    private int total;

    @JsonProperty("link")
    private List<Link> link;

    @JsonProperty("entry")
    private List<Entry> entry;

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
    public static class Link {
        @JsonProperty("relation")
        private String relation;

        @JsonProperty("url")
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Entry {
        @JsonProperty("fullUrl")
        private String fullUrl;

        @JsonProperty("resource")
        private Resource resource;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Resource {
        @JsonProperty("resourceType")
        private String resourceType;

        @JsonProperty("id")
        private String id;

        @JsonProperty("meta")
        private Meta meta;

        @JsonProperty("extension")
        private List<Extension> extension;

        @JsonProperty("identifier")
        private List<Identifier> identifier;

        @JsonProperty("status")
        private String status;

        @JsonProperty("date")
        private String date;

        @JsonProperty("securityLabel")
        private List<SecurityLabel> securityLabel;

        @JsonProperty("content")
        private List<Content> content;

        @JsonProperty("context")
        private Context context;
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Identifier {
        @JsonProperty("system")
        private String system;

        @JsonProperty("value")
        private String value;
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
    public static class SecurityLabel {
        @JsonProperty("coding")
        private List<Coding> coding;
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
    public static class Content {
        @JsonProperty("attachment")
        private Attachment attachment;

        @JsonProperty("format")
        private Format format;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Attachment {
        @JsonProperty("id")
        private String id;

        @JsonProperty("contentType")
        private String contentType;

        @JsonProperty("language")
        private String language;

        @JsonProperty("url")
        private String url;

        @JsonProperty("size")
        private Integer size;

        @JsonProperty("hash")
        private String hash;

        @JsonProperty("title")
        private String title;

        @JsonProperty("creation")
        private String creation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Format {
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
    public static class Context {
        @JsonProperty("facilityType")
        private FacilityType facilityType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FacilityType {
        @JsonProperty("coding")
        private List<Coding> coding;
    }
}
