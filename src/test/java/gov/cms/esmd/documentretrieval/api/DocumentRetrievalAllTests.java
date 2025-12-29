package gov.cms.esmd.documentretrieval.api;

import gov.cms.esmd.auth.api.AuthenticationAPIHandler;
import gov.cms.esmd.common.util.GuidUtil;
import gov.cms.esmd.config.YamlConfigLoaderJackson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DocumentRetrievalAllTests {

    @Test
    void test_binaryClient() throws Exception {

        var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");
        var config = loader.getConfig();
        var success = false;

        var commonSharedGuid = GuidUtil.generateGuid();


        var token = new AuthenticationAPIHandler(config.getAppSettings()).getToken();
        if (token.isSuccess()) {

            var binaryClientHandler = new BinaryClientAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
            var binaryClientResponse = binaryClientHandler.getBinaryFileData(commonSharedGuid);
            success = binaryClientResponse.isSuccess();


        }
        Assertions.assertTrue(success);



    }

    @Test
    void test_deliveryConfirmation() throws Exception {

        var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");
        var config = loader.getConfig();
        var success = false;



        var token = new AuthenticationAPIHandler(config.getAppSettings()).getToken();
        if (token.isSuccess()) {

         var deliveryConfirmationHandler = new DeliveryConfirmationAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
            var deliveryConfirmationResponse = deliveryConfirmationHandler.processDeliveryConfirmation();
            success = deliveryConfirmationResponse.isSuccess();





        }
        Assertions.assertTrue(success);



    }

}
