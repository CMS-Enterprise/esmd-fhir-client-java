package gov.cms.esmd.bundlesubmission.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)

public class UploadClinicalDocumentResponse {
    private String status;
    private String message;
    private String filename;
    private String s3uri;
    private ErrorDetail error;
}
