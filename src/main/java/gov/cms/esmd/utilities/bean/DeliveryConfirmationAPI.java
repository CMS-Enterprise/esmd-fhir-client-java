package gov.cms.esmd.utilities.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeliveryConfirmationAPI extends CommonAppSettings {

    private String accept;
    private DeliveryConfirmationAPIRequest request;

}
