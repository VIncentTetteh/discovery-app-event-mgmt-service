package com.discovery.eventservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackSubscriptionInitResponse {

    private boolean status;
    private String message;
    private SubscriptionData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubscriptionData {

        private Long id;

        @JsonProperty("subscription_code")
        private String subscriptionCode;

        @JsonProperty("email_token")
        private String emailToken;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("cron_expression")
        private String cronExpression;

        @JsonProperty("next_payment_date")
        private String nextPaymentDate;

        @JsonProperty("open_invoice")
        private String openInvoice;

        @JsonProperty("status")
        private String status;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;
    }
}
