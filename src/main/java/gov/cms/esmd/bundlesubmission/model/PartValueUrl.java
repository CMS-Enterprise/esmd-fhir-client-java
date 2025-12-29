package gov.cms.esmd.bundlesubmission.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PartValueUrl {
    private String name;
    private String valueUrl;
}
