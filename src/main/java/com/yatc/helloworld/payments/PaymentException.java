package com.yatc.helloworld.payments;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
