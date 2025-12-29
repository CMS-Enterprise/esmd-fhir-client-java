package gov.cms.esmd.bundlesubmission.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetail {

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Message")
    private String message;

    @JsonProperty("X-Amz-Expires")
    private String xAmzExpires;

    @JsonProperty("Expires")
    private String expires;
    @JsonProperty("ServerTime")
    private String serverTime;
    @JsonProperty("RequestId")
    private String requestId;
    @JsonProperty("HostId")
    private String hostId;
}
