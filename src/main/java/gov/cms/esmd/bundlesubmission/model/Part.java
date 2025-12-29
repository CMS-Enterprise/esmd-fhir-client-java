package gov.cms.esmd.bundlesubmission.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Part {

    private String name;
    private List<Part2> part;
    private String valueString;
    private String valueUrl;
}
