package com.yatc.helloworld.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkOutUrl;
}
