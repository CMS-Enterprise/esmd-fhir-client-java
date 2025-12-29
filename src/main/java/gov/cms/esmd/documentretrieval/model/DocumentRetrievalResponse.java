package gov.cms.esmd.documentretrieval.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import gov.cms.esmd.common.model.Entry;
import gov.cms.esmd.common.model.Issue;
import gov.cms.esmd.common.model.Link;
import gov.cms.esmd.common.model.Meta;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentRetrievalResponse {

    private String resourceType;
    private String id;
    private Meta meta;
    private String type;
    private Integer total;
    private List<Link> link;
    private List<Entry> entry;
    private List<Issue> issue;
}
