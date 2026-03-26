
# esmd-fhir-client-java
**Version:** 1.0.0

# esmd-fhir-client-java- Enhanced FHIR Client Library



[![OpenJDK 17](https://img.shields.io/badge/OpenJDK-17-green.svg)](https://jdk.java.net/17/)

[![Code Quality](https://img.shields.io/badge/code%20quality-A-green.svg)](https://github.com/your-repo/esmd-fhir-client-java)

[![Tests](https://img.shields.io/badge/tests-passing-green.svg)](https://github.com/your-repo/esmd-fhir-client-java)



A comprehensive, production-ready JAVA client library for interacting with esMD FHIR services. This client library support document submission, document retrieval, error handing and notifications, provider service registration, provider delivery acknowledgments, and esMD system reference data retrieval.



## 🚀 Key Features

### Core Functionality

-  **Bundle Submission**: Submit FHIR bundles with presigned URL support

-  **Practitioner Management**: Search, create, and manage practitioner resources

-  **Document Reference Handling**: Upload and manage document references

-  **Notification Processing**: Handle transaction notifications and lists



### Enhanced Features ✨

-  **🔒 Robust Authentication**: OAuth2 with token caching and automatic refresh

-  **🛡️ Error Handling**: Comprehensive exception hierarchy with detailed error context

-  **✅ Input Validation**: Comprehensive validation for all inputs

-  **📝 Comprehensive Logging**: Structured logging with configurable levels and audit trails

-  **🧪 Extensive Testing**: 90%+ test coverage with unit and integration tests

-  **📦 Modular Design**: Import only the modules you need for your use case




## 📦 Installation

### Development Installation

### 🧑‍💻 Using Visual Studio Code
1. Install Visual Studio Code
2. Install the **Extension Pack for Java**
3. Install **Java 17 (JDK)**
4. Open a terminal in the project folder
5. Run:
  ```bash
   code .
   ```


### 🧑‍💻 Using IntelliJ IDEA Community Edition
1. Install IntelliJ IDEA Community Edition
2. Install Java 17 (JDK)
3. Open IntelliJ IDEA
4. Click **Open**
5. Select the project folder (or `pom.xml` / `build.gradle` file)

---
### 📦 Requirements
- Java JDK 17 installed

### Verify installation:
```bash
java -version
```

## ⚙️ Configuration - YAML

### 🔐 Required Configuration

Before running the project, make sure to set the following values in the YAML configuration file located at`src/main/resources/app-settings-config-jackson.yml`

```yaml

clientId="your-client-id"
clientSecret="your-client-secret"

```
⚠️ **Note:** Replace the placeholder values with your actual credentials before starting the application.

### ⚙️ Configuration File (`app-settings-config-jackson.yml`)

The configuration file is located at`src/main/resources/app-settings-config-jackson.yml`.

<details>


---



## ⚙️ Configuration - YAML

### 🧾 Complete YAML Configuration

```yaml
appSettings:
  httpClientRequestTimeOutSeconds: 2.0
  baseFileLocationFolder: "C:\\Users\\AmerMaqsood\\Downloads"
  fhirServerUrl: "https://terminology.esmdval.cms.gov:8099"
  endPointBaseUrl: "https://val.cpiapigateway.cms.gov"

  authenticationAPI:
    clientId: "your_client_id"
    clientSecret: "your_client_secret"
    scope: "hih/esmdfhir"
    endpointURL: "${endPointBaseUrl}/api/esmdf/auth/generate"
    contentType: "application/json"
    httpClientRequestTimeOutSeconds: 2.0
    userAgent: "Refyne-FHIR-Client/1.0"

  presignedURLAPI:
    endpointURL: "${endPointBaseUrl}/api/esmdf/v1/fhir/DocumentReference/$generate-presigned-url"
    contentType: "application/fhir+json"
    accept: "application/fhir+json"
    httpClientRequestTimeOutSeconds: 2.0
    request:
      resourceType: "Parameters"
      parameter:
        - name: "organizationid"
          valueString: "urn:oid:123.456.657.126"
        - name: "fileinfo"
          part:
            - name: "filename"
              valueString: "presigned_url_xml_file.xml"
            - name: "content-md5"
              valueString: ""
            - name: "filesize"
              valueString: ""
            - name: "mimetype"
              valueString: "application/xml"

  uploadClinicalDocumentAPI:
    contentType: "application/xml"
    httpClientRequestTimeOutSeconds: 4.0
    fileName: "presigned_url_xml_file.xml"
    contentMD5: ""

  bundleSubmissionAPI:
    endpointURL: "${endPointBaseUrl}/api/esmdf/v1/fhir"
    contentType: "application/fhir+json"
    accept: "application/fhir+json"
    httpClientRequestTimeOutSeconds: 4.0
    request:
      resourceType: Bundle
      type: transaction
      entry:
        - fullUrl: ""
          resource:
            resourceType: List
            id: ""
            meta:
              profile:
                - "${fhirServerUrl}/fhir/StructureDefinition/Esmd-ListSubmissionSet"
              security:
                - system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
                  code: V
                  display: very restricted
            extension:
              - url: "${fhirServerUrl}/fhir/StructureDefinition/Esmd-Ext-LinesOfBusinessId"
                valueCode: "11.1"
              - url: "${fhirServerUrl}/fhir/StructureDefinition/Esmd-Ext-OrganizationId"
                valueString: "urn:oid:123.456.657.126"
          request:
            method: POST
            url: List

        - fullUrl: ""
          resource:
            resourceType: DocumentReference
            id: ""
            meta:
              profile:
                - "${fhirServerUrl}/fhir/StructureDefinition/Esmd-DocumentReference"
              security:
                - system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
                  code: V
                  display: very restricted
            status: current
            title: Submission Set Title
            content:
              - attachment:
                  id: ""
                  contentType: ""
                  url: ""
                  size: 1
                  hash: ""
                  title: ""
                  creation: ""
            context:
              facilityType:
                coding:
                  - system: "${fhirServerUrl}/fhir/CodeSystem/Esmd-CS-FacilityTypeCodes"
                    code: hih
                    display: Health Information Handler (HIH)
                  - system: "${fhirServerUrl}/fhir/CodeSystem/Esmd-CS-FacilityTypeCodes"
                    code: hcp
                    display: Health Care Provider
                  - system: "${fhirServerUrl}/fhir/CodeSystem/Esmd-CS-FacilityTypeCodes"
                    code: cms-rc
                    display: CMS Review Contractor

  notificationRetrievalAPI:
    endpointURL: "${endPointBaseUrl}/api/esmdf/v1/fhir/List"
    accept: "application/fhir+json"
    httpClientRequestTimeOutSeconds: 4.0
    requestParameters:
      - name: "transaction-status-type"
        value: "ready-to-download"
        inject: false
      - name: "requesttype"
        value: "ADMIN_ERROR"
        inject: false
      - name: "uniqueid"
        value: ""
        inject: false

  documentRetrievalAPI:
    endpointURL: "${endPointBaseUrl}/api/esmdf/v1/fhir/DocumentReference"
    accept: "application/fhir+json"
    httpClientRequestTimeOutSeconds: 4.0
    requestParameters:
      - name: "transaction-status-type"
        value: "ready-to-download"
        inject: false
      - name: "uniqueid"
        value: ""
        inject: false

  deliveryConfirmationAPI:
    endpointURL: "${endPointBaseUrl}/api/esmdf/v1/fhir/List"
    accept: "application/fhir+json"
    contentType: "application/fhir+json"
    httpClientRequestTimeOutSeconds: 4.0
    request:
      resourceType: "List"
      id: "0187157c-da3f-4a74-b687-423bab362754"
      status: current

  practitionerAPI:
    endpointURL: "${endPointBaseUrl}/api/esmdf/v1/fhir/Practitioner/{id}"
    accept: "application/fhir+json"
    contentType: "application/fhir+json"
    httpClientRequestTimeOutSeconds: 4.0
    request:
      resourceType: Practitioner
      id: "PRACT1234"
      name:
        - family: "Gaven"
          given: ["Radems"]
          prefix: ["Dr"]
          suffix: ["Ph.D"]
      telecom:
        - system: "phone"
          value: "551-333-6141"
          use: "work"
      address:
        - use: "work"
          line: ["1003 MILLERS DR"]
          city: "ELLICOTT CITY"
          state: "MD"
          postalCode: "21043"
      gender: "female"
      active: true
```

</details>

# 📊 Overview - Configuration Guide Summary

## 🔹 Global Settings

| Key | Type | Description | Example |
|-----|------|------------|--------|
| httpClientRequestTimeOutSeconds | Number | API timeout duration | 2.0 |
| baseFileLocationFolder | String | Local file storage path | `C:\Users\AmerMaqsood\Downloads` |
| fhirServerUrl | String | Base FHIR server | `https://terminology.esmdval.cms.gov:8099` |
| endPointBaseUrl | String | API gateway base URL | `https://val.cpiapigateway.cms.gov` |

---

## 🔐 Authentication API

| Key | Type | Description |
|-----|------|------------|
| clientId | String | Unique client identifier |
| clientSecret | String | Secret credential (secure) |
| scope | String | Access scope |
| endpointURL | String | Auth endpoint |
| contentType | String | Request format |
| userAgent | String | Client identifier |

---

## 📄 Presigned URL API

| Key | Type | Description |
|-----|------|------------|
| endpointURL | String | Generate upload URL |
| contentType | String | Request format |
| accept | String | Response format |

### 🔸 Nested Request (Presigned URL)

| Key | Type | Description |
|-----|------|------------|
| resourceType | String | FHIR resource type (`Parameters`) |
| parameter[].name | String | Parameter name |
| parameter[].valueString | String | Parameter value |

---

## 📦 Bundle Submission API

| Key | Type | Description |
|-----|------|------------|
| resourceType | String | Always `Bundle` |
| type | String | Transaction type |
| endpointURL | String | Submission endpoint |

### 🔸 Nested Bundle Structure

| Key | Type | Description |
|-----|------|------------|
| entry[] | Array | List of resources |
| entry[].resource | Object | FHIR resource (List, DocumentReference) |
| entry[].request.method | String | HTTP method (POST) |
| entry[].request.url | String | Resource endpoint |

---

## 📤 Upload Clinical Document

| Key | Type | Description |
|-----|------|------------|
| contentType | String | File type (`application/xml`) |
| fileName | String | File name |
| contentMD5 | String | File checksum |

---

## 📥 Retrieval APIs

| API | Key | Description |
|-----|-----|------------|
| Notification | transaction-status-type | Filter status |
| Document | uniqueid | Document identifier |

---

## ✅ Delivery Confirmation API

| Key | Type | Description |
|-----|------|------------|
| resourceType | String | `List` |
| contained[] | Array | Embedded resources |
| issue[].diagnostics | String | Status message |

---

## 👩‍⚕️ Practitioner API

| Key | Type | Description |
|-----|------|------------|
| resourceType | String | `Practitioner` |
| name.family | String | Last name |
| name.given[] | Array | First name(s) |
| telecom.value | String | Contact number |
| address.city | String | City |

---

## 📦 Binary API

| Key | Type | Description |
|-----|------|------------|
| endpointURL | String | Binary fetch endpoint |
| fileNameId | String | File identifier |

---

## 🔁 Bundle Practitioner API

| Key | Type | Description |
|-----|------|------------|
| resourceType | String | `Bundle` |
| type | String | `batch` |
| entry[].resource | Object | Practitioner resource |
| entry[].request.method | String | HTTP method (PUT) |

---

## 🧠 Key Concepts

| Term | Meaning |
|------|--------|
| FHIR | Healthcare data exchange standard |
| Bundle | Collection of resources |
| DocumentReference | Metadata of a document |
| Presigned URL | Secure upload link |

---


# 🏃‍♂️ Quick Start

## 🧩 Modular Library Usage


### 🧩 .NET Using Statements

The following `using` statements are included in the project to organize namespaces and simplify code access. They allow you to use classes, models, and services without fully qualifying each type.

---

#### 🔹 FHIR Client APIs

These imports provide access to various FHIR-related operations:

```java
import gov.cms.esmd.auth.api.AuthenticationAPIHandler;  
import gov.cms.esmd.bundlesubmission.api.BundlePractitionerAPIHandler;  
import gov.cms.esmd.bundlesubmission.api.BundleSubmissionAPIHandler;  
import gov.cms.esmd.bundlesubmission.api.ClinicalDocumentUploadAPIHandler;  
import gov.cms.esmd.bundlesubmission.api.PreSignedURLAPIHandler;  
import gov.cms.esmd.common.util.GuidUtil;  
import gov.cms.esmd.config.YamlConfigLoaderJackson;  
import gov.cms.esmd.documentretrieval.api.BinaryClientAPIHandler;  
import gov.cms.esmd.documentretrieval.api.DeliveryConfirmationAPIHandler;  
import gov.cms.esmd.documentretrieval.api.DocumentRetrievalAPIHandler;  
import gov.cms.esmd.notification.api.NotificationAPIHandler;  
import gov.cms.esmd.practitioner.api.PractitionerAPIHandler;

```


### 🔹 Purpose of FHIR Client APIs

# 📦 FHIR API Imports Overview

These imports provide access to **various FHIR-related operations** for authentication, document handling, bundle submissions, practitioner management, notifications, and utility functions in the ESMD framework.

---

## 🔐 Authentication

| Import | Description |
|--------|-------------|
| `gov.cms.esmd.auth.api.AuthenticationAPIHandler` | Handles authentication with the FHIR server and generates access tokens for API calls. |

---

## 📄 Bundle Submission & Clinical Documents

| Import | Description |
|--------|-------------|
| `gov.cms.esmd.bundlesubmission.api.BundleSubmissionAPIHandler` | Submits bundles of FHIR resources (e.g., Lists, DocumentReferences) to the FHIR server. |
| `gov.cms.esmd.bundlesubmission.api.BundlePractitionerAPIHandler` | Submits bundles specifically containing Practitioner resources for registration or updates. |
| `gov.cms.esmd.bundlesubmission.api.ClinicalDocumentUploadAPIHandler` | Handles the uploading of clinical documents to the FHIR server. |
| `gov.cms.esmd.bundlesubmission.api.PreSignedURLAPIHandler` | Generates presigned URLs for secure uploads of files (used for document storage before submission). |

---

## 📥 Document Retrieval & Delivery

| Import | Description |
|--------|-------------|
| `gov.cms.esmd.documentretrieval.api.DocumentRetrievalAPIHandler` | Retrieves DocumentReference resources from the FHIR server. |
| `gov.cms.esmd.documentretrieval.api.BinaryClientAPIHandler` | Downloads the actual file content (Binary resources) referenced in DocumentReferences. |
| `gov.cms.esmd.documentretrieval.api.DeliveryConfirmationAPIHandler` | Confirms that a document or transaction has been delivered successfully. |

---

## 🛎️ Notifications

| Import | Description |
|--------|-------------|
| `gov.cms.esmd.notification.api.NotificationAPIHandler` | Retrieves notifications or transaction status updates from the FHIR server (e.g., ready-to-download alerts). |

---

## 👩‍⚕️ Practitioner Management

| Import | Description |
|--------|-------------|
| `gov.cms.esmd.practitioner.api.PractitionerAPIHandler` | Handles CRUD operations for Practitioner resources, including registration and updates. |

---

## 🛠️ Utilities & Configuration

| Import | Description |
|--------|-------------|
| `gov.cms.esmd.common.util.GuidUtil` | Provides helper functions for generating GUIDs or unique identifiers for FHIR resources. |
| `gov.cms.esmd.config.YamlConfigLoaderJackson` | Loads application configuration from YAML files into Java objects using Jackson library. |

---

### ✅ Summary

These imports together enable your application to:

1. Authenticate securely with the FHIR server.
2. Submit, retrieve, and manage clinical documents and bundles.
3. Handle Practitioner resources efficiently.
4. Manage notifications and delivery confirmations.
5. Utilize utility functions for configuration and GUID generation.

This modular design aligns with FHIR standards and ensures **separation of concerns**, making the application maintainable and extensible.



### 📦 Main Console Application

**MainConsoleApplication.java** is a console application that demonstrates how to use the FHIR client APIs.

# 🖥️ `MainConsoleApplication` Overview

This Java class demonstrates a **console-based application** that interacts with the ESMD FHIR APIs to authenticate, upload documents, submit bundles, retrieve notifications, and manage practitioners.

---

## 📦 Package & Imports

```java
package gov.cms.esmd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.cms.esmd.auth.api.AuthenticationAPIHandler;
import gov.cms.esmd.bundlesubmission.api.BundlePractitionerAPIHandler;
import gov.cms.esmd.bundlesubmission.api.BundleSubmissionAPIHandler;
import gov.cms.esmd.bundlesubmission.api.ClinicalDocumentUploadAPIHandler;
import gov.cms.esmd.bundlesubmission.api.PreSignedURLAPIHandler;
import gov.cms.esmd.common.util.GuidUtil;
import gov.cms.esmd.config.YamlConfigLoaderJackson;
import gov.cms.esmd.documentretrieval.api.BinaryClientAPIHandler;
import gov.cms.esmd.documentretrieval.api.DeliveryConfirmationAPIHandler;
import gov.cms.esmd.documentretrieval.api.DocumentRetrievalAPIHandler;
import gov.cms.esmd.notification.api.NotificationAPIHandler;
import gov.cms.esmd.practitioner.api.PractitionerAPIHandler;
import lombok.extern.slf4j.Slf4j;
```


| Import                             | Purpose                                                         |
| ---------------------------------- | --------------------------------------------------------------- |
| `AuthenticationAPIHandler`         | Handles authentication and token retrieval.                     |
| `BundleSubmissionAPIHandler`       | Submits FHIR bundles containing documents/resources.            |
| `BundlePractitionerAPIHandler`     | Submits bundles specifically containing Practitioner resources. |
| `ClinicalDocumentUploadAPIHandler` | Uploads clinical documents using pre-signed URLs.               |
| `PreSignedURLAPIHandler`           | Generates secure URLs for uploading files.                      |
| `BinaryClientAPIHandler`           | Fetches actual file content from DocumentReference resources.   |
| `DeliveryConfirmationAPIHandler`   | Confirms document delivery on the server.                       |
| `DocumentRetrievalAPIHandler`      | Retrieves document metadata and references.                     |
| `NotificationAPIHandler`           | Retrieves transaction or system notifications.                  |
| `PractitionerAPIHandler`           | Handles CRUD operations for Practitioner resources.             |
| `YamlConfigLoaderJackson`          | Loads configuration from a YAML file.                           |
| `GuidUtil`                         | Generates unique identifiers for resources.                     |
| `ObjectMapper`                     | Serializes/deserializes JSON responses.                         |
| `@Slf4j`                           | Provides logging capability.                                    |


## 📦 Code File

```java
@Slf4j
public class MainConsoleApplication {

    public static void main(String[] args) {
        try {
            // Load YAML configuration
            var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");
            var config = loader.getConfig();

            // Print some key config values
            log.info("HTTP Timeout: {}", config.getAppSettings().getHttpClientRequestTimeOutSeconds());
            log.info("FHIR Server URL: {}", config.getAppSettings().getFhirServerUrl());

            // Generate a shared GUID
            var commonSharedGuid = GuidUtil.generateGuid();

            // Authenticate and retrieve token
            var token = new AuthenticationAPIHandler(config.getAppSettings()).getToken();

            if (token.isSuccess()) {
                // Notifications
                var notification = new NotificationAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken()).getNotifications();

                // Presigned URL for document upload
                var preSignedURLHandler = new PreSignedURLAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var preSignedURL = preSignedURLHandler.getPreSignedURLAsync(commonSharedGuid);

                // Upload clinical documents
                var clinicalDocumentUploadHandler = new ClinicalDocumentUploadAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                preSignedURLHandler.processPreSignedURLResponse(preSignedURL.getResponse()).forEach(url -> {
                    var uploadDocumentResponse = clinicalDocumentUploadHandler.uploadClinicalDocument(url.getPartValueUrl().getValueUrl(), url.getPartValueString().getValueString());
                    
                    // Submit bundle after successful document upload
                    var bundleSubmissionAPIHandler = new BundleSubmissionAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                    bundleSubmissionAPIHandler.processBundleSubmissionRequest(uploadDocumentResponse.getResponse(), commonSharedGuid);
                });

                // Bundle Practitioner submission
                var bundlePractitionerHandler = new BundlePractitionerAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                bundlePractitionerHandler.processBundlePractitionerRequest(commonSharedGuid);

                // Binary retrieval
                var binaryClientHandler = new BinaryClientAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                binaryClientHandler.getBinaryFileData(commonSharedGuid);

                // Delivery confirmation
                var deliveryConfirmationHandler = new DeliveryConfirmationAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                deliveryConfirmationHandler.processDeliveryConfirmation();

                // Practitioner retrieval
                var practitionerHandler = new PractitionerAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                practitionerHandler.processPractitionerRequest(null);

                // Document retrieval
                var documentRetrievalHandler = new DocumentRetrievalAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                documentRetrievalHandler.getDocumentRetrievalData();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
```

🔑 **Key Points**

- **Configuration Loading:** Uses `YamlConfigLoaderJackson` to load all API endpoints, credentials, and settings from `app-settings-config-jackson.yml`.
- **Authentication:** `AuthenticationAPIHandler` fetches a token to authorize API calls.
- **Notification Retrieval:** Polls for transaction notifications or system messages.
- **Document Upload:** Uses `PreSignedURLAPIHandler` and `ClinicalDocumentUploadAPIHandler` to upload clinical documents securely.
- **Bundle Submission:** After uploading, documents are submitted as FHIR bundles via `BundleSubmissionAPIHandler`.
- **Practitioner Management:** Handles Practitioner creation or updates through bundle submission or direct API calls.
- **Document Retrieval & Binary Download:** Fetches both metadata (`DocumentReference`) and actual file content (`Binary`).
- **Delivery Confirmation:** Ensures that transactions/documents are successfully delivered.
- **Logging:** Uses `Slf4j` for detailed logging of every API operation and response.
- **Error Handling:** Catches exceptions and logs errors at each step to ensure visibility into failures.

## ⚙️ Loading Configuration

Before interacting with the FHIR APIs, the application needs to load configuration and FHIR client API settings information.
  
---

```java
// Load application configuration from YAML file
var loader = new YamlConfigLoaderJackson("app-settings-config-jackson.yml");

// Retrieve the parsed configuration object
var config = loader.getConfig();
```

**Explanation:**

| Code | Purpose |
|------|---------|
| `YamlConfigLoaderJackson("app-settings-config-jackson.yml")` | Loads the YAML configuration file containing API endpoints, credentials, timeouts, and other settings. |
| `loader.getConfig()` | Parses the YAML file and returns a **typed configuration object** that your application can use to access all settings. |

This is typically the **first step** in your application so that all other handlers (authentication, bundle submission, document upload, etc.) can use the configuration values safely.


## 🔗 Shared GUID for tracking purpose
```java
// Generate a unique GUID to be shared across multiple API requests
var commonSharedGuid = GuidUtil.generateGuid();

// Log the generated GUID for tracking purposes
log.info("Common Shared GUID {}", commonSharedGuid);

```


**Explanation:**

| Code | Purpose |
|------|---------|
| `GuidUtil.generateGuid()` | Generates a globally unique identifier (GUID/UUID) that can be used to correlate multiple API requests or resources within a single workflow. |
| `log.info("Common Shared GUID {}", commonSharedGuid)` | Logs the GUID to the console or log file so developers or support staff can trace operations across different API calls. |

This is useful for **tracking a transaction or request** end-to-end, especially in workflows involving multiple FHIR API calls like document upload, bundle submission, and practitioner updates.

## `AuthenticationAPIHandler.getToken()`

```java
// Log that the application is about to request an authentication token
log.info("Getting Token....");

// Call the Authentication API to obtain an access token using app settings
var token = new AuthenticationAPIHandler(config.getAppSettings()).getToken();
// Check if the token retrieval was successful  
if (token.isSuccess()) {  
	// Log the retrieved access token  
	log.info("token value is - {}", token.getResponse().getAccessToken());  
}

```

**Explanation:**

| Code | Purpose |
|------|---------|
| `AuthenticationAPIHandler(config.getAppSettings()).getToken()` | Creates a handler instance and requests an OAuth2 token using configuration from the YAML file. |
| `token.isSuccess()` | Checks whether the token request succeeded before attempting to use the token. |
| `log.info("token value is - {}", token.getResponse().getAccessToken())` | Logs the access token for debugging or verification. This token is required for authenticating subsequent API requests. |

This ensures that the application **only proceeds if authentication succeeds**, preventing errors in downstream API calls.

This step is **essential for secure API access**, as most subsequent API calls require this token for authorization.

## `PreSignedURLAPIHandler.getPreSignedURLAsync() & Processing`

```java
// Log the start of PreSigned URL processing
log.info("Now Processing PreSignedURL request!");

// Initialize the PreSigned URL handler with configuration and access token
var preSignedURLHandler = new PreSignedURLAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());

// Request pre-signed URLs asynchronously using the shared GUID
var preSignedURL = preSignedURLHandler.getPreSignedURLAsync(commonSharedGuid);

// Check if the PreSigned URL request succeeded
if (preSignedURL.isSuccess()) {
    // Log the retrieved pre-signed URL response
    log.info("PreSignedURL Retrieved Success - {}", new ObjectMapper().writeValueAsString(preSignedURL.getResponse()));

    // Process the response to extract individual URL details for document upload
    var urlList = preSignedURLHandler.processPreSignedURLResponse(preSignedURL.getResponse());
}

```


**Explanation:**

| Code | Purpose |
|------|---------|
| `PreSignedURLAPIHandler(config, token)` | Creates a handler for interacting with the PreSigned URL API using app settings and the authentication token. |
| `getPreSignedURLAsync(commonSharedGuid)` | Sends a request to generate pre-signed URLs for document upload, tied to a unique GUID for tracking. |
| `preSignedURL.isSuccess()` | Ensures the request succeeded before attempting to process the response. |
| `processPreSignedURLResponse(preSignedURL.getResponse())` | Converts the API response into a list of URLs ready for uploading documents. |
| `log.info(...)` | Logs API responses in JSON format for debugging and verification. |

This ensures documents can be uploaded **securely via unique pre-signed URLs** and prepares the data for the next step in the workflow.

### `ClinicalDocumentUploadAPIHandler.uploadClinicalDocument() & Processing`

```java
if (preSignedURL.isSuccess()) {
    // Log the successful retrieval of pre-signed URLs
    log.info("PreSignedURL Retrieved Success - {}", new ObjectMapper().writeValueAsString(preSignedURL.getResponse()));

    // Process the PreSigned URL response into a list of URLs
    var urlList = preSignedURLHandler.processPreSignedURLResponse(preSignedURL.getResponse());

    // Initialize the Clinical Document Upload handler
    var clinicalDocumentUploadHandler = new ClinicalDocumentUploadAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());

    // Iterate over each URL in the list and upload the corresponding document
    urlList.forEach(url -> {
        var uploadDocumentResponse = clinicalDocumentUploadHandler.uploadClinicalDocument(
            url.getPartValueUrl().getValueUrl(),
            url.getPartValueString().getValueString()
        );
    });
}

```


**Explanation:**

| Code | Purpose |
|------|---------|
| `urlList.forEach(url -> {...})` | Loops through each pre-signed URL to upload the corresponding document. |
| `ClinicalDocumentUploadAPIHandler(config, token)` | Initializes the handler for uploading clinical documents to the FHIR server. |
| `uploadClinicalDocument(url, fileName)` | Uploads a single document using the pre-signed URL and its associated file name. |
| `log.info(...)` | Logs the response of the PreSigned URL retrieval for debugging and verification. |

This block ensures that **all documents associated with the pre-signed URLs are uploaded** to the FHIR server before moving to the bundle submission step.


### `ClinicalDocumentUpload & BundleSubmission Processing`

```java
if (preSignedURL.isSuccess()) {
    log.info("PreSignedURL Retrieved Success - {}", new ObjectMapper().writeValueAsString(preSignedURL.getResponse()));
    
    // Convert PreSignedURL response to a list for processing
    var urlList = preSignedURLHandler.processPreSignedURLResponse(preSignedURL.getResponse());
    
    // Initialize handler for clinical document upload
    var clinicalDocumentUploadHandler = new ClinicalDocumentUploadAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());

    // Process each URL to upload the document and submit the bundle
    urlList.forEach(url -> {
        var uploadDocumentResponse = clinicalDocumentUploadHandler.uploadClinicalDocument(
            url.getPartValueUrl().getValueUrl(), 
            url.getPartValueString().getValueString()
        );

        try {
            if (uploadDocumentResponse.isSuccess()) {
                log.info("upload document response success - {}", new ObjectMapper().writeValueAsString(uploadDocumentResponse.getResponse()));

                log.info("Now Processing Bundle Submission request!");
                var bundleSubmissionAPIHandler = new BundleSubmissionAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
                var bundleSubmissionResponse = bundleSubmissionAPIHandler.processBundleSubmissionRequest(
                    uploadDocumentResponse.getResponse(), 
                    commonSharedGuid
                );

                if (bundleSubmissionResponse.isSuccess()) {
                    log.info("processing bundleSubmission success - {}", new ObjectMapper().writeValueAsString(bundleSubmissionResponse.getResponse()));
                } else {
                    log.error("processing bundleSubmission Failed - {}", bundleSubmissionResponse.getError());
                }

            } else {
                log.error("upload document Failed - {}", uploadDocumentResponse.getError());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    });

} else {
    log.error("PreSignedURL Retrieval Failed - {}", preSignedURL.getError());
}

```


**Explanation:**

| Code Segment | Purpose |
|--------------|---------|
| `urlList.forEach(url -> {...})` | Iterates over each pre-signed URL to upload the associated document. |
| `ClinicalDocumentUploadAPIHandler.uploadClinicalDocument()` | Uploads a document using its URL and file name. |
| `BundleSubmissionAPIHandler.processBundleSubmissionRequest()` | Submits the uploaded document as a FHIR Bundle to the server. |
| `if (uploadDocumentResponse.isSuccess())` | Ensures upload succeeded before submitting bundle. |
| `log.info` / `log.error` | Logs success or failure messages for tracing each step. |
| `try-catch(JsonProcessingException)` | Handles JSON serialization errors when logging response objects. |

This block handles **document upload and immediate bundle submission**, ensuring each uploaded clinical document is wrapped and sent as a FHIR Bundle.

### `BundlePractitioner, Binary, DeliveryConfirmation, Practitioner & Document Retrieval Processing`

```java
// Process Bundle Practitioner
log.info("Now Processing Bundle Practitioner request!");
var bundlePractitionerHandler = new BundlePractitionerAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
var bundlePractitioner = bundlePractitionerHandler.processBundlePractitionerRequest(commonSharedGuid);
if (bundlePractitioner.isSuccess()) {
    log.info("Successfully process bundlePractitioner  - {}", new ObjectMapper().writeValueAsString(bundlePractitioner.getResponse()));
} else {
    log.error("Bundle Practitioner Failed - {}", bundlePractitioner.getError());
}

// Process Binary Client
log.info("Now Processing Binary Client request!");
var binaryClientHandler = new BinaryClientAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
var binaryClientResponse = binaryClientHandler.getBinaryFileData(commonSharedGuid);
if (binaryClientResponse.isSuccess()) {
    log.info("Successfully processed Binary client Request  - {}", new ObjectMapper().writeValueAsString(binaryClientResponse.getResponse()));
} else {
    log.error("Binary Client Request processing Failed - {}", binaryClientResponse.getError());
}

// Process Delivery Confirmation
log.info("Now Processing Delivery Confirmation request!");
var deliveryConfirmationHandler = new DeliveryConfirmationAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
var deliveryConfirmationResponse = deliveryConfirmationHandler.processDeliveryConfirmation();
if (deliveryConfirmationResponse.isSuccess()) {
    log.info("Successfully processed Delivery Confirmation Request  - {}", new ObjectMapper().writeValueAsString(deliveryConfirmationResponse.getResponse()));
} else {
    log.error("Delivery Confirmation Request processing Failed - {}", deliveryConfirmationResponse.getError());
}

// Process Practitioner
log.info("Now Processing Practitioner request!");
var practitionerHandler = new PractitionerAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
var practitionerResponse = practitionerHandler.processPractitionerRequest(null);
if (practitionerResponse.isSuccess()) {
    log.info("Successfully processed practitioner Request  - {}", new ObjectMapper().writeValueAsString(practitionerResponse.getResponse()));
} else {
    log.error("practitioner Request processing Failed - {}", practitionerResponse.getError());
}

// Process Document Retrieval
log.info("Now Processing Document Retrieval request!");
var documentRetrievalHandler = new DocumentRetrievalAPIHandler(config.getAppSettings(), token.getResponse().getAccessToken());
var documentRetrievalHandlerResponse = documentRetrievalHandler.getDocumentRetrievalData();
if (documentRetrievalHandlerResponse.isSuccess()) {
    log.info("Successfully processed document retrieval Request  - {}", new ObjectMapper().writeValueAsString(documentRetrievalHandlerResponse.getResponse()));
} else {
    log.error("Document Retrieval Request processing Failed - {}", documentRetrievalHandlerResponse.getError());
}
```


**Explanation:**

| Section | Purpose |
|---------|---------|
| `BundlePractitionerAPIHandler` | Submits practitioner-related FHIR resources as a bundle. |
| `BinaryClientAPIHandler` | Fetches binary file data for documents using a shared GUID. |
| `DeliveryConfirmationAPIHandler` | Confirms delivery status of previously submitted bundles or documents. |
| `PractitionerAPIHandler` | Retrieves practitioner information from the FHIR server. |
| `DocumentRetrievalAPIHandler` | Retrieves clinical documents and other resources from the FHIR server. |
| `log.info` / `log.error` | Provides detailed logging of successes or failures for each operation. |

This block essentially **executes multiple API workflows sequentially**, handling FHIR resource submissions, document retrieval, and status confirmations. It ensures each step is verified before proceeding to the next.



### Logging and Monitoring
This logging approach enhances observability. It uses **Logback** in Java via the `slf4j` API and a `logback.xml` configuration file. It provides multiple log levels to serve different purposes:

- **Info** – gives a high-level overview of events or failures for operational monitoring.
- **Warn** – emphasizes that an issue might need attention or investigation.
- **Debug** – provides detailed internal data for troubleshooting by developers.

---

### Logging Examples with `Logback`

This demonstrates logging at multiple levels using **SLF4J** with **Logback** in Java.

```java
// Info Logging: Logs a concise message indicating a significant event or operation status
log.info("Operation completed with status {}", status);

// Warn Logging: Logs a warning to indicate potential problems that may need attention
log.warn("Potential issue detected: {}", warningMessage);

// Debug Logging: Logs full details of the response or internal state for in-depth debugging purposes
log.debug("Full response data: {}", responseData);
```

---

### Benefits

- Improves visibility into API failures.
- Supports multiple audiences: operations (Info), monitoring (Warn), and developers (Debug).
- Helps with audit trails by recording the failed responses.
- Facilitates faster troubleshooting with detailed contextual information.




## 🏗️ Architecture



### Project Structure

```
  
esmd-fhir-client-java  
|   .gitignore  
|   pom.xml  
|   PROJECT_STRUCTURE.txt  
|   README.md  
|     
\---src  
    +---main  
    |   +---java  
    |   |   \---gov  
    |   |       \---cms  
    |   |           \---esmd  
    |   |               |   MainConsoleApplication.java  
    |   |               |     
    |   |               +---auth  
    |   |               |   +---api  
    |   |               |   |       AuthApiClient.java  
    |   |               |   |       AuthenticationAPIHandler.java  
    |   |               |   |         
    |   |               |   +---bean  
    |   |               |   |       AuthInfo.java  
    |   |               |   |       AuthResponse.java  
    |   |               |   |       ErrorResponse.java  
    |   |               |   |         
    |   |               |   \---model  
    |   |               |           Token.java  
    |   |               |             
    |   |               +---bundlesubmission  
    |   |               |   +---api  
    |   |               |   |       BundlePractitionerAPIHandler.java  
    |   |               |   |       BundleSubmissionAPIHandler.java  
    |   |               |   |       ClinicalDocumentUploadAPIHandler.java  
    |   |               |   |       PreSignedURLAPIHandler.java  
    |   |               |   |         
    |   |               |   \---model  
    |   |               |           BundlePractitionerResponse.java  
    |   |               |           BundleSubmissionResponse.java  
    |   |               |           ErrorDetail.java  
    |   |               |           Parameter.java  
    |   |               |           Part.java  
    |   |               |           Part2.java  
    |   |               |           PartValueString.java  
    |   |               |           PartValueUrl.java  
    |   |               |           PreSignedURLInfo.java  
    |   |               |           PreSignedURLResponse.java  
    |   |               |           UploadClinicalDocumentResponse.java  
    |   |               |           ValueDuration.java  
    |   |               |             
    |   |               +---common  
    |   |               |   +---model  
    |   |               |   |       Address.java  
    |   |               |   |       Attachment.java  
    |   |               |   |       ClientResponse.java  
    |   |               |   |       Coding.java  
    |   |               |   |       Contained.java  
    |   |               |   |       Content.java  
    |   |               |   |       Context.java  
    |   |               |   |       Details.java  
    |   |               |   |       Entry.java  
    |   |               |   |       Extension.java  
    |   |               |   |       FacilityType.java  
    |   |               |   |       Format.java  
    |   |               |   |       Identifier.java  
    |   |               |   |       Issue.java  
    |   |               |   |       Item.java  
    |   |               |   |       Link.java  
    |   |               |   |       Meta.java  
    |   |               |   |       Name.java  
    |   |               |   |       Outcome.java  
    |   |               |   |       Resource.java  
    |   |               |   |       Response.java  
    |   |               |   |       Security.java  
    |   |               |   |       SecurityLabel.java  
    |   |               |   |       Telecom.java  
    |   |               |   |         
    |   |               |   \---util  
    |   |               |           Constants.java  
    |   |               |           CryptoUtils.java  
    |   |               |           CurlUtil.java  
    |   |               |           DataFileWriterUtils.java  
    |   |               |           DateTimeUtil.java  
    |   |               |           FileUtil.java  
    |   |               |           GuidUtil.java  
    |   |               |           HttpClientUtil.java  
    |   |               |           JsonUtil.java  
    |   |               |           ParsedUrl.java  
    |   |               |           URLUtils.java  
    |   |               |             
    |   |               +---config  
    |   |               |   |   YamlConfigLoaderJackson.java  
    |   |               |   |     
    |   |               |   \---model  
    |   |               |           AppConfigJackson.java  
    |   |               |             
    |   |               +---documentretrieval  
    |   |               |   +---api  
    |   |               |   |       BinaryAPIHandler.java  
    |   |               |   |       BinaryClientAPIHandler.java  
    |   |               |   |       DeliveryConfirmationAPIHandler.java  
    |   |               |   |       DocumentRetrievalAPIHandler.java  
    |   |               |   |       DocumentRetrievalAPIHandlerAnother.java  
    |   |               |   |         
    |   |               |   +---bean  
    |   |               |   |       BinaryAPIFailedResponse.java  
    |   |               |   |       BinaryAPISuccessResponse.java  
    |   |               |   |       DeliveryConfirmationAPIFailedResponse.java  
    |   |               |   |       DeliveryConfirmationAPIResponse.java  
    |   |               |   |       DocumentRetrievalAPIFailedResponse.java  
    |   |               |   |       DocumentRetrievalAPISuccessResponse.java  
    |   |               |   |         
    |   |               |   \---model  
    |   |               |           BinaryClientResponse.java  
    |   |               |           DeliveryConfirmationResponse.java  
    |   |               |           DocumentRetrievalResponse.java  
    |   |               |             
    |   |               +---notification  
    |   |               |   +---api  
    |   |               |   |       NotificationAPIHandler.java  
    |   |               |   |         
    |   |               |   \---model  
    |   |               |           NotificationResponse.java  
    |   |               |             
    |   |               +---practitioner  
    |   |               |   +---api  
    |   |               |   |       PractitionerAPIHandler.java  
    |   |               |   |         
    |   |               |   \---model  
    |   |               |           PractitionerResponse.java  
    |   |               |             
    |   |               \---utilities  
    |   |                   |   ConfigurationManager.java  
    |   |                   |   Constants.java  
    |   |                   |   JSONUtility.java  
    |   |                   |   PropertiesUtils.java  
    |   |                   |   ValidatorUtility.java  
    |   |                   |     
    |   |                   \---bean  
    |   |                           BinaryAPI.java  
    |   |                           CommonAppSettings.java  
    |   |                           DeliveryConfirmationAPI.java  
    |   |                           DeliveryConfirmationAPIRequest.java  
    |   |                           DocumentRetrievalAPI.java  
    |   |                             
    |   \---resources  
    |           api-properties.yml  
    |           app-settings-config-jackson.yml  
    |           logback-test.xml  
    |           logback.xml  
    |             
    \---test  
        \---java  
            \---gov  
                \---cms  
                    \---esmd  
                        +---auth  
                        |   \---api  
                        |           AuthApiClientTest.java  
                        |             
                        +---bundlesubmission  
                        |       BundleSubmissionAllHandlersTest.java  
                        |         
                        +---documentretrieval  
                        |   \---api  
                        |           BinaryAPIHandlerTest.java  
                        |           DocumentRetrievalAllTests.java  
                        |           DocumentRetrievalAPIHandlerAnotherTest.java  
                        |             
                        +---main  
                        |       MainAppTest.java  
                        |         
                        +---notification  
                        |       NotificationAPIHandlerTest.java  
                        |         
                        \---practitioner  
                                PractitionerAPIHandlerTest.java

```



## 🧪 Building and Tests



## Maven Build and Test Instructions for ESMD Project

This guide shows how to **build the project** using the `pom.xml` file and **run tests** from the `tests` folder using Maven.

---

## 1. Prerequisites

- **Java JDK 17+** installed
- **Maven 3.6+** installed
- Project source code with `pom.xml`
- Network access for integration tests if needed

Check installed versions:

```bash
java -version
mvn -version
```

## 2. Build the Project and Run Tests


```bash
### Navigate to the project root folder

cd path/to/project
### Clean and compile the project

mvn clean compile

### Package the project into a JAR/WAR

mvn clean package
### To skip tests during packaging

mvn clean install -DskipTests

### Run All Tests

mvn test

### Run Specific Test Method

mvn -Dtest=ClassNameTest#methodName test
```

### Development Workflow

Follow these steps to contribute to the project:

1. **Fork the repository**  
   Create your own copy of the repository under your GitHub account.

2. **Create a feature branch**  
   Use a descriptive branch name, e.g., `feature/add-auth-module`.

3. **Make your changes with tests**  
   Implement the feature or fix, and include corresponding unit tests.

4. **Ensure all quality checks pass**  
   Run linting, formatting, and tests to verify your changes.

5. **Submit a pull request**  
   Open a PR from your feature branch to the main repository, describing the changes and referencing any related issues.




### Code Review Checklist

Ensure the following before merging any code:

1. **Code follows style guidelines**  
   Maintain consistent formatting, naming conventions, and project style.

2. **All tests pass**  
   Unit, integration, and end-to-end tests should succeed.

3. **Type hints are present**  
   All functions, variables, and parameters should have proper types.

4. **Documentation is updated**  
   README, inline comments, and API docs reflect the new changes.

5. **Error handling is comprehensive**  
   All potential errors are properly caught and handled.

6. **Performance impact is considered**  
   Changes should not introduce significant performance regressions.



**`esmd-fhir-client-java`** - Production-ready FHIR client with enterprise-grade reliability and performance.