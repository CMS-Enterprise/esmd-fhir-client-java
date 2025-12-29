package gov.cms.esmd.bundlesubmission.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cms.esmd.common.model.Issue;
import gov.cms.esmd.common.model.Meta;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreSignedURLResponse {
    private String resourceType;
    private String id;
    private List<Parameter> parameter;
    private Meta meta;
    private List<Issue> issue;
}
