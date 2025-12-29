package gov.cms.esmd.documentretrieval.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import gov.cms.esmd.documentretrieval.bean.DocumentRetrievalAPISuccessResponse.Meta;
import gov.cms.esmd.documentretrieval.bean.DocumentRetrievalAPIFailedResponse.Issue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BinaryAPIFailedResponse {

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("issue")
    private List<Issue> issue;

}