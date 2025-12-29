package gov.cms.esmd.common.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientResponse<T> {
    private T response;
    private boolean success;
    private String error;

    public ClientResponse() {
    }

    public ClientResponse(T response, boolean success, String error) {
        this.response = response;
        this.success = success;
        this.error = error;
    }

}