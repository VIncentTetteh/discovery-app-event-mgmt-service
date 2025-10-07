package com.discovery.eventservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackCustomerResponse {

    private boolean status;
    private String message;
    private CustomerData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerData {
        private Long id;
        private String first_name;
        private String last_name;
        private String email;

        @JsonProperty("customer_code")
        private String customerCode;

        @JsonProperty("integration")
        private Long integrationId;

        @JsonProperty("domain")
        private String domain;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;
    }
}
