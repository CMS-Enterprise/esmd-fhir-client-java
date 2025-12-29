package gov.cms.esmd.auth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String access_token;
    private int expires_in;
    private String token_type;
    private String error;
    private String statusCode;
    private String message;
    private String result;
}
