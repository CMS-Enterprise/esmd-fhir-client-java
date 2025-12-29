package gov.cms.esmd.documentretrieval.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BinaryAPISuccessResponse {

    @JsonProperty("resourceType")
    private String resourceType;

    @JsonProperty("id")
    private String id;

    @JsonProperty("meta")
    private DocumentRetrievalAPISuccessResponse.Meta meta;

    @JsonProperty("contentType")
    private String contentType;

    @JsonProperty("data")
    private String data;

}
