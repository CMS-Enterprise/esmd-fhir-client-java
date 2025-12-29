package gov.cms.esmd.utilities.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BinaryAPI extends CommonAppSettings {

    private String accept;
    private String fileNameId;

}