package gov.cms.esmd.utilities;

import gov.cms.esmd.auth.bean.AuthInfo;
import gov.cms.esmd.auth.bean.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ValidatorUtility {
    private static final Logger log = LoggerFactory.getLogger(ValidatorUtility.class);

    // Validates the AuthInfo object for required fields
    public List<ErrorResponse> validateAuthInfo(AuthInfo authInfo) {
        log.info("Starting validateAuthInfo method.");

        if (authInfo == null) {
            log.warn("AuthInfo object is null.");
            return generateResponseForNull();
        }

        List<ErrorResponse> errorMessages = new ArrayList<>();
        addErrorMessageIfNull(
                authInfo.getUsername(),
                "USERNAME_REQUIRED",
                "Username is required in authInfo. Please correct and resubmit.",
                errorMessages);
        addErrorMessageIfNull(
                authInfo.getPassword(),
                "PASSWORD_REQUIRED",
                "Password is required in authInfo. Please correct and resubmit.",
                errorMessages);
        addErrorMessageIfNull(
                authInfo.getClientkey(),
                "CLIENTKEY_REQUIRED",
                "Client Key is required in authInfo. Please correct and resubmit.",
                errorMessages);
        addErrorMessageIfNull(
                authInfo.getClientsecret(),
                "CLIENTSECRET_REQUIRED",
                "Client Secret is required in authInfo. Please correct and resubmit.",
                errorMessages);
        log.info("Finished validateAuthInfo method.");
        return errorMessages.size() > 0 ? errorMessages : null;
    }

    private void addErrorMessageIfNull(
            String value, String errorCode, String errorMessage, List<ErrorResponse> errorMessages) {
        if (value == null || value.isEmpty()) {
            log.warn("Validation failed: {}", errorMessage);
            errorMessages.add(new ErrorResponse(errorCode, errorMessage, errorMessage));
        }
    }

    public List<ErrorResponse> validateAuthInfoFromProperties(Properties properties) {
        log.info("Starting validateAuthInfoFromProperties method.");
        List<ErrorResponse> errorMessageList = new ArrayList<>();

        String[] requiredProperties = {
                "credentials.username:USERNAME_REQUIRED:user name is required, either pass as a parameter or integrate in yaml file",
                "credentials.password:PASSWORD_REQUIRED:Password is required, either pass as a parameter or integrate in yaml file",
                "credentials.access-key:CLIENTID_REQUIRED:Client id is required, either pass as a parameter or integrate in yaml file",
                "credentials.secret-key:SECRETKEY_REQUIRED:Secret Key is required, either pass as a parameter or integrate in yaml file",
                "api.scope.auth:SCOPE_REQUIRED:API Scope is required, either pass as a parameter or integrate in yaml file",
                "credentials.mailboxid:MAILBOX_REQUIRED:MAIL BOX is required, either pass as a parameter or integrate in yaml file"
        };

        for (String property : requiredProperties) {
            String[] parts = property.split(":");
            String propName = parts[0];
            String errorCode = parts[1];
            String errorMessage = parts[2];

            if (properties.getProperty(propName) == null) {
                log.warn("Validation failed: " + errorMessage);
                errorMessageList.add(new ErrorResponse(errorCode, errorMessage, errorMessage));
            }
        }

        String[] urlProperties = {
                "api.url.admin-error-notification",
                "api.url.auth",
                "api.url.pa-reject-notification",
                "api.environment.prod",
                "api.environment.uat"
        };

        boolean hasNullUrl = false;
        for (String urlProperty : urlProperties) {
            if (properties.getProperty(urlProperty) == null) {
                hasNullUrl = true;
                break;
            }
        }

        if (hasNullUrl) {
            log.warn("Validation failed: API url is required.");
            errorMessageList.add(new ErrorResponse("APIURL_REQUIRED", "API url is required", "API url is required"));
        }

        log.info("Finished validateAuthInfoFromProperties method.");
        return errorMessageList.size() > 0 ? errorMessageList : null;
    }

    private ErrorResponse generateErrorMessage(String code, String message, String description) {
        log.debug("Generating error message: " + code + ", " + message);
        return new ErrorResponse(code, message, description);
    }

    private ErrorResponse generateErrorMessage(String code, Properties properties) {
        log.debug("Generating error message for code: " + code);
        String errorMessage = properties.getProperty("errorCodes." + code);
        return new ErrorResponse(code, errorMessage, errorMessage);
    }

    public List<ErrorResponse> generateResponseForNull() {
        log.info("Starting generateResponseForNull method.");
        try {
            ErrorResponse errorMessage = generateErrorMessage(
                    "AUTH_INFO_EMPTY",
                    "Username, Password, Clientid and Clientsecret are required. Please correct and resubmit.",
                    "Username, Password, Clientid and Clientsecret are required. Please correct and resubmit."
            );

            List<ErrorResponse> errorMessagesList = new ArrayList<>();
            errorMessagesList.add(errorMessage);

            log.info("Finished generateResponseForNull method.");
            return errorMessagesList;
        } catch (Exception ex) {
            log.error("An error occurred while generating the notification response.", ex);
            throw ex;
        }
    }
}
