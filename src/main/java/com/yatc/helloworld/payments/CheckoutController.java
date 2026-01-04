package com.yatc.helloworld.payments;

import com.yatc.helloworld.dtos.ErrorDto;
import com.yatc.helloworld.exceptions.CartEmptyException;
import com.yatc.helloworld.exceptions.CartNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest checkoutRequest) {
        return checkoutService.checkout(checkoutRequest);
    }

    @PostMapping("/webhook")
    public void webhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class, PaymentException.class})
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
