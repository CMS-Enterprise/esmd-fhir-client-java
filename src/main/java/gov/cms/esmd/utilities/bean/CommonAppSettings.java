package gov.cms.esmd.utilities.bean;

import lombok.Data;

@Data
public class CommonAppSettings {

    private String contentType;
    private String endpointURL;
    private double httpClientRequestTimeOutSeconds = 2.0;

}
