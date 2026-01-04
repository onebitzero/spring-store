package com.yatc.helloworld.payments;

import com.yatc.helloworld.entities.Cart;
import com.yatc.helloworld.entities.Order;
import com.yatc.helloworld.exceptions.CartEmptyException;
import com.yatc.helloworld.exceptions.CartNotFoundException;
import com.yatc.helloworld.repositories.CartRepository;
import com.yatc.helloworld.repositories.OrderRepository;
import com.yatc.helloworld.services.AuthService;
import com.yatc.helloworld.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        Cart cart = cartRepository.findById(request.getCartId()).orElse(null);

        if (cart == null || cart.isEmpty()) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        Order order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);

        try {
            CheckoutSession checkoutSession = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), checkoutSession.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);

            throw e;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    Order order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }
}
