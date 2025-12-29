package gov.cms.esmd.common.util;



import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ParsedUrl {

    private String baseUrl;
    private String resourcePath;
    private Map<String, String> params; // optional

    public ParsedUrl(String baseUrl, String resourcePath, Map<String, String> params) {
        this.baseUrl = baseUrl;
        this.resourcePath = resourcePath;
        this.params = params;
    }

}
