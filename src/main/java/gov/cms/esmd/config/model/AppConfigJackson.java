package gov.cms.esmd.config.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppConfigJackson {
    private AppSettings appSettings;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AppSettings {
        private double httpClientRequestTimeOutSeconds;
        private String baseFileLocationFolder;
        private String fhirServerUrl;
        private String endPointBaseUrl;

        private AuthenticationAPI authenticationAPI;
        private PresignedURLAPI presignedURLAPI;
        private UploadClinicalDocumentAPI uploadClinicalDocumentAPI;
        private BundleSubmissionAPI bundleSubmissionAPI;
        private NotificationRetrievalAPI notificationRetrievalAPI;
        private DocumentRetrievalAPI documentRetrievalAPI;
        private DeliveryConfirmationAPI deliveryConfirmationAPI;
        private PractitionerAPI practitionerAPI;
        private BinaryAPI binaryAPI;
        private BundlePractitionerAPI bundlePractitionerAPI;

        // --- Authentication ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AuthenticationAPI {
            private String clientId;
            private String clientSecret;
            private String scope;
            private String endpointURL;
            private String contentType;
            private double httpClientRequestTimeOutSeconds;
            private String userAgent;
        }


        // --- Presigned URL API ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PresignedURLAPI {
            private String endpointURL;
            private String contentType;
            private String accept;
            private double httpClientRequestTimeOutSeconds;
            private Request request;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Request {
                private String resourceType;
                private String id;
                private List<Parameter> parameter;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Parameter {
                    private String name;
                    private String valueString;
                    private List<Part> part;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Part {
                        private String name;
                        private String valueString;
                    }
                }
            }
        }

        // --- Upload Clinical Document ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UploadClinicalDocumentAPI {
            private String contentType;
            private double httpClientRequestTimeOutSeconds;
            private String fileName;
            private String contentMD5;
        }

        // --- Bundle Submission API ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BundleSubmissionAPI {

            private String endpointURL;
            private String contentType;
            private String accept;
            private double httpClientRequestTimeOutSeconds;
            private Request request;

            // =====================================================
            // Bundle Request
            // =====================================================
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Request {

                private String resourceType;
                private String id;
                private Meta meta;
                private String type;
                private String timestamp;
                private List<Entry> entry;

                // ---------------- Meta ----------------
                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Meta {
                    private List<String> profile;
                    private List<Security> security;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Security {
                        private String system;
                        private String code;
                        private String display;
                    }
                }

                // ---------------- Entry ----------------
                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Entry {

                    private String fullUrl;
                    private Resource resource;
                    private RequestAction request;

                    // ---------- Resource ----------
                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Resource {

                        private String resourceType;
                        private String id;
                        private Meta meta;

                        // ---- List ----
                        private List<Extension> extension;
                        private String status;
                        private String mode;
                        private String title;
                        private String date;
                        private List<ListEntry> entry;

                        // ---- DocumentReference ----
                        private List<Identifier> identifier;
                        private List<Category> category;
                        private List<SecurityLabel> securityLabel;
                        private List<Content> content;
                        private Context context;

                        // ---------- Extension ----------
                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Extension {
                            private String url;
                            private String valueCode;
                            private String valueString;
                        }

                        // ---------- List Entry ----------
                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class ListEntry {
                            private Item item;

                            @Data
                            @NoArgsConstructor
                            @AllArgsConstructor
                            @JsonInclude(JsonInclude.Include.NON_NULL)
                            @JsonIgnoreProperties(ignoreUnknown = true)
                            public static class Item {
                                private String reference;
                            }
                        }

                        // ---------- DocumentReference ----------
                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Identifier {
                            private String system;
                            private String value;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Category {
                            private List<Coding> coding;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class SecurityLabel {
                            private List<Coding> coding;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Coding {
                            private String system;
                            private String code;
                            private String display;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Content {
                            private Attachment attachment;
                            private Format format;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Attachment {
                            private String id;
                            private String contentType;
                            private String url;
                            private Long size;
                            private String hash;
                            private String title;
                            private String creation;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Format {
                            private String system;
                            private String code;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Context {
                            private FacilityType facilityType;

                            @Data
                            @NoArgsConstructor
                            @AllArgsConstructor
                            @JsonInclude(JsonInclude.Include.NON_NULL)
                            @JsonIgnoreProperties(ignoreUnknown = true)
                            public static class FacilityType {
                                private List<Coding> coding;
                            }
                        }
                    }

                    // ---------- RequestAction ----------
                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class RequestAction {
                        private String method;
                        private String url;
                    }
                }
            }
        }


        // --- Notification Retrieval API ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class NotificationRetrievalAPI {
            private String endpointURL;
            private String accept;
            private double httpClientRequestTimeOutSeconds;
            private List<RequestParameter> requestParameters;
        }

        // --- Document Retrieval API ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DocumentRetrievalAPI {
            private String endpointURL;
            private String accept;
            private double httpClientRequestTimeOutSeconds;
            private List<RequestParameter> requestParameters;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RequestParameter {
            private String name;
            private String value;
            private boolean inject;
        }

        // --- Delivery Confirmation API ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DeliveryConfirmationAPI {
            private String endpointURL;
            private String accept;
            private String contentType;
            private double httpClientRequestTimeOutSeconds;
            private Request request;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Request {
                private String resourceType;
                private String id;
                private Meta meta;
                private List<Contained> contained;
                private List<Extension> extension;
                private String status;
                private String mode;
                private String title;
                private String date;
                private List<Entry> entry;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Meta {
                    private List<String> profile;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Contained {
                    private String resourceType;
                    private String id;
                    private Meta meta;
                    private List<Issue> issue;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Meta {
                        private List<String> profile;
                        private List<Security> security;

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Security {
                            private String system;
                            private String code;
                            private String display;
                        }
                    }

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Issue {
                        private String severity;
                        private String code;
                        private String diagnostics;
                    }
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Extension {
                    private String url;
                    private String valueString;
                    private String valueCode;
                    private String valueDateTime;
                    private String valueDate;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Entry {
                    private Item item;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Item {
                        private String reference;

                    }
                }
            }
        }

        // --- Practitioner API ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PractitionerAPI {
            private String endpointURL;
            private String accept;
            private String contentType;
            private double httpClientRequestTimeOutSeconds;
            private Request request;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Request {
                private String resourceType;
                private String id;
                private Meta meta;
                private List<Name> name;
                private List<Telecom> telecom;
                private List<Address> address;
                private String gender;
                private boolean active;
                private List<Extension> extension;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Meta {
                    private List<String> profile;
                    private List<Security> security;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Security {
                        private String system;
                        private String code;
                        private String display;
                    }
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Name {
                    private String family;
                    private List<String> given;
                    private List<String> prefix;
                    private List<String> suffix;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Telecom {
                    private String system;
                    private String value;
                    private String use;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Address {
                    private String use;
                    private List<String> line;
                    private String city;
                    private String state;
                    private String postalCode;
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Extension {
                    private String url;
                    private String valueString;
                    private String valueCode;
                    private String valueDateTime;
                    private String valueDate;
                }
            }
        }

        // --- Binary API ---
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BinaryAPI {
            private String endpointURL;
            private String accept;
            private String fileNameId;
            private double httpClientRequestTimeOutSeconds;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class BundlePractitionerAPI {
            private String endpointURL;
            private String contentType;
            private String accept;
            private double httpClientRequestTimeOutSeconds;
            private Request request;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Request {
                private String resourceType;
                private String id;
                private Meta meta;
                private String type;
                private String timestamp;
                private List<Entry> entry;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Meta {
                    private List<String> profile;
                    private List<Security> security;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Security {
                        private String system;
                        private String code;
                        private String display;
                    }
                }

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                @JsonInclude(JsonInclude.Include.NON_NULL)
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Entry {
                    private String fullUrl;
                    private Resource resource;
                    private RequestAction request;

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Resource {
                        private String resourceType;
                        private String id;
                        private Meta meta;
                        private List<Telecom> telecom;
                        private List<Name> name;
                        private List<Address> address;
                        private String gender;
                        private Boolean active;
                        private List<Extension> extension;

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Meta {
                            private List<String> profile;
                            private List<Security> security;

                            @Data
                            @NoArgsConstructor
                            @AllArgsConstructor
                            @JsonInclude(JsonInclude.Include.NON_NULL)
                            @JsonIgnoreProperties(ignoreUnknown = true)
                            public static class Security {
                                private String system;
                                private String code;
                                private String display;
                            }
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Telecom {
                            private String system;
                            private String value;
                            private String use;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Name {
                            private String family;
                            private List<String> given;
                            private List<String> prefix;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Address {
                            private String use;
                            private List<String> line;
                            private String city;
                            private String state;
                            private String postalCode;
                        }

                        @Data
                        @NoArgsConstructor
                        @AllArgsConstructor
                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @JsonIgnoreProperties(ignoreUnknown = true)
                        public static class Extension {
                            private String url;
                            private String valueString;
                            private String valueCode;
                            private String valueDate;
                        }
                    }

                    @Data
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class RequestAction {
                        private String method;
                        private String url;
                    }
                }
            }
        }

    }
}
