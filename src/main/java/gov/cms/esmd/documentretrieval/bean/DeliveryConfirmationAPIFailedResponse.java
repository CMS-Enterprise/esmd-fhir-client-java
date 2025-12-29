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
public class DeliveryConfirmationAPIFailedResponse {

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("meta")
    private DocumentRetrievalAPISuccessResponse.Meta meta;

    @JsonProperty("issue")
    private List<DocumentRetrievalAPIFailedResponse.Issue> issue;

    // Reuse previously defined classes: Meta, Issue
    // Meta and Issue can be imported from the same package or defined here if needed
}
