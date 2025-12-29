package gov.cms.esmd.bundlesubmission.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValueDuration {
    private Double value;
    private String unit;
    private String system;
    private String code;
}
