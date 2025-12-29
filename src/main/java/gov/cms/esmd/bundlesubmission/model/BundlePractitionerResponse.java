package gov.cms.esmd.bundlesubmission.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cms.esmd.common.model.Entry;
import gov.cms.esmd.common.model.Identifier;
import gov.cms.esmd.common.model.Link;
import gov.cms.esmd.common.model.Meta;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BundlePractitionerResponse {
    private String resourceType;
    private String id;
    private Meta meta;
    private Identifier identifier;
    private String type;
    private List<Link> link;
    private List<Entry> entry;
}
