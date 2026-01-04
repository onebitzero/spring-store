package com.yatc.helloworld.payments;

import com.yatc.helloworld.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);

    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
