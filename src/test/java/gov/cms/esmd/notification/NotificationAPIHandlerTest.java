package gov.cms.esmd.notification;


import gov.cms.esmd.auth.api.AuthenticationAPIHandler;

import gov.cms.esmd.config.YamlConfigLoaderJackson;
import gov.cms.esmd.config.model.AppConfigJackson;
import gov.cms.esmd.notification.api.NotificationAPIHandler;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for NotificationAPIHandler
 */


@ExtendWith(MockitoExtension.class)
class NotificationAPIHandlerTest {


    @Test
    void test_success() throws Exception {

        var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");
        var config = loader.getConfig();
        var success = false;


        var token = new AuthenticationAPIHandler(config.getAppSettings()).getToken();
        if (token.isSuccess()) {

            var notification = new NotificationAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken()).getNotifications();
            success = notification.isSuccess();


        }
        Assertions.assertTrue(success);



    }

    @Test
    void test_failure() throws Exception {

        YamlConfigLoaderJackson loader = new YamlConfigLoaderJackson("app-settings-config.yml");
        AppConfigJackson config = loader.getConfig();
        var success = true;


        var notification = new NotificationAPIHandler(config.getAppSettings(), "fake-failed-token").getNotifications();
        success = notification.isSuccess();
        Assertions.assertFalse(success);



    }
}
