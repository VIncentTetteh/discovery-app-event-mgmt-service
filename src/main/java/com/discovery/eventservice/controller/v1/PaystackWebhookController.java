package com.discovery.eventservice.controller.v1;

import com.discovery.eventservice.config.PaystackConfig;
import com.discovery.eventservice.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;

@RestController
@RequestMapping("/api/paystack/webhook")
@RequiredArgsConstructor
@Slf4j
public class PaystackWebhookController {

    private final SubscriptionRepository subscriptionRepository;
    private final PaystackConfig paystackConfig;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("x-paystack-signature") String signature,
            @RequestBody String rawBody) {

        try {
            // ✅ Verify Paystack webhook signature
            String computedSignature = hmacSha512(rawBody, paystackConfig.getSECRET_KEY());
            if (!computedSignature.equals(signature)) {
                log.warn("Invalid Paystack webhook signature!");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
            }

            // Parse payload
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = mapper.readValue(rawBody, Map.class);
            String event = (String) payload.get("event");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            log.info("Received Paystack webhook: {}", event);

            if ("invoice.payment_succeeded".equals(event)) {
                String subscriptionCode = (String) data.get("subscription_code");
                subscriptionRepository.findByPaystackSubscriptionCode(subscriptionCode).ifPresent(sub -> {
                    sub.setEndDate(sub.getEndDate().plusMonths(1));
                    sub.setActive(true);
                    subscriptionRepository.save(sub);
                    log.info("Subscription renewed successfully: {}", subscriptionCode);
                });
            } else if ("subscription.disable".equals(event)) {
                String subscriptionCode = (String) data.get("subscription_code");
                subscriptionRepository.findByPaystackSubscriptionCode(subscriptionCode).ifPresent(sub -> {
                    sub.setActive(false);
                    subscriptionRepository.save(sub);
                    log.info("Subscription disabled: {}", subscriptionCode);
                });
            }

            return ResponseEntity.ok("Webhook processed");

        } catch (Exception ex) {
            log.error("Error handling Paystack webhook: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    private String hmacSha512(String data, String secret) throws Exception {
        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512_HMAC.init(secret_key);
        return HexFormat.of().formatHex(sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}

