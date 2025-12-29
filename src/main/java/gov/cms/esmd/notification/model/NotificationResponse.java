package gov.cms.esmd.notification.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cms.esmd.common.model.Entry;
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
public class NotificationResponse {

    private String resourceType;
    private String id;
    private Meta meta;
    private String type;
    private List<Link> link;
    private List<Entry> entry;

}
