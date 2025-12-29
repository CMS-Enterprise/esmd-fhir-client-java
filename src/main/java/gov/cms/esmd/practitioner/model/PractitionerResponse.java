package gov.cms.esmd.practitioner.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.cms.esmd.common.model.*;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PractitionerResponse {

    private String resourceType;
    private String id;
    private Identifier identifier;
    private Meta meta;
    private List<Name> name;
    private List<Telecom> telecom;
    private List<Address> address;
    private String gender;
    private Boolean active;
    private List<Extension> extension;
    private List<Issue> issue;
}

