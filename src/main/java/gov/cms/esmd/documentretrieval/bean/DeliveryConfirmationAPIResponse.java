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
public class DeliveryConfirmationAPIResponse {

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("id")
    private String id;

    @JsonProperty("meta")
    private DocumentRetrievalAPISuccessResponse.Meta meta;

    @JsonProperty("contained")
    private List<Contained> contained;

    @JsonProperty("extension")
    private List<DocumentRetrievalAPISuccessResponse.Extension> extension;

    @JsonProperty("status")
    private String status;

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("title")
    private String title;

    @JsonProperty("date")
    private String date;

    @JsonProperty("entry")
    private List<DocumentRetrievalAPISuccessResponse.Entry> entry;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Contained {

        @JsonProperty("resourceType")
        private String resourceType;

        @JsonProperty("id")
        private String id;

        @JsonProperty("meta")
        private DocumentRetrievalAPISuccessResponse.Meta meta;

        @JsonProperty("issue")
        private List<DocumentRetrievalAPIFailedResponse.Issue> issue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {

        @JsonProperty("reference")
        private String reference;
    }
}
