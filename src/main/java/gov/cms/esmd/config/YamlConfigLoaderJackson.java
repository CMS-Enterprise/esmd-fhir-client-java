package gov.cms.esmd.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import gov.cms.esmd.common.util.JsonUtil;
import gov.cms.esmd.config.model.AppConfigJackson;
import lombok.Getter;

import java.io.InputStream;
import java.util.Map;

@Getter
public class YamlConfigLoaderJackson {

    private final AppConfigJackson config;

    public YamlConfigLoaderJackson(String yamlFilePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(yamlFilePath)) {
            if (inputStream == null) {
                throw new RuntimeException("YAML file not found: " + yamlFilePath);
            }

            // Use Jackson YAML mapper
            ObjectMapper yamlMapper = new YAMLMapper();
            AppConfigJackson yamlConfig = yamlMapper.readValue(inputStream, AppConfigJackson.class);

            // Replace placeholders dynamically
            Map<String, String> replacements = Map.of(
                    "${endPointBaseUrl}", yamlConfig.getAppSettings().getEndPointBaseUrl(),
                    "${fhirServerUrl}", yamlConfig.getAppSettings().getFhirServerUrl()
            );

            this.config = JsonUtil.replaceValues(yamlConfig, replacements, AppConfigJackson.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML", e);
        }
    }

}
