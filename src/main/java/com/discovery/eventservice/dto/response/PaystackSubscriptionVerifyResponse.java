package com.discovery.eventservice.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackSubscriptionVerifyResponse {

    private boolean status;
    private String message;
    private SubscriptionData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubscriptionData {

        private Long id;

        @JsonProperty("customer")
        private Object customer; // or a nested CustomerData object if needed

        @JsonProperty("plan")
        private Object plan; // could be mapped similarly to a PlanData class

        @JsonProperty("subscription_code")
        private String subscriptionCode;

        @JsonProperty("email_token")
        private String emailToken;

        private String status;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("next_payment_date")
        private String nextPaymentDate;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;
    }
}

