package gov.cms.esmd.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigurationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);
    private static Yaml yaml = new Yaml();
    private static Map<String, Object> obj;

    private ConfigurationManager() {

    }
    private static void loadYamlFile() {
        LOGGER.info("Start ConfigurationManager:loadYamlFile() load configuration yml file...");
        InputStream inputStream = ConfigurationManager.class.getClassLoader().getResourceAsStream("api-properties.yml");
        obj = yaml.load(inputStream);
        LOGGER.info("End ConfigurationManager:loadYamlFile() load configuration yml file...");
    }



/*
YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(new PathResource(file.toPath()));
    factory.afterPropertiesSet();
 */
    public static synchronized Map<String, Object> getInstance() {
        LOGGER.info("Start ConfigurationManager:getInstance() ...");
        if (obj == null) {
            loadYamlFile();
        }
        LOGGER.info("End ConfigurationManager:getInstance() ...");
        return obj;
    }

}
