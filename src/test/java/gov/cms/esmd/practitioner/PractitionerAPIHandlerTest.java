package gov.cms.esmd.practitioner;

import gov.cms.esmd.auth.api.AuthenticationAPIHandler;
import gov.cms.esmd.config.YamlConfigLoaderJackson;
import gov.cms.esmd.practitioner.api.PractitionerAPIHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PractitionerAPIHandlerTest {


    @Test
    void test() throws Exception {

        var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");
        var config = loader.getConfig();
        var success = false;

        var token = new AuthenticationAPIHandler(config.getAppSettings()).getToken();
        if (token.isSuccess()) {

            var practitionerHandler = new PractitionerAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
            var practitionerResponse = practitionerHandler.processPractitionerRequest(null);
            success = practitionerResponse.isSuccess();


        }
        Assertions.assertTrue(success);


    }


}
