package gov.cms.esmd.main;

import gov.cms.esmd.config.YamlConfigLoaderJackson;
import org.junit.jupiter.api.Test;

public class MainAppTest {

    @Test
    public void getConfig() {
        var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");
        var config = loader.getConfig();
        System.out.println("Base folder: " + config.getAppSettings().getBaseFileLocationFolder());


    }
}

