package gov.cms.esmd.auth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfo {
    private String username;
    private String password;
    private String clientkey;
    private String clientsecret;
}
