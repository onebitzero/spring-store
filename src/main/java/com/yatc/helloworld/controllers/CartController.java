package com.yatc.helloworld.controllers;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.yatc.helloworld.dtos.AddToCartRequest;
import com.yatc.helloworld.dtos.CartDto;
import com.yatc.helloworld.dtos.CartItemDto;
import com.yatc.helloworld.dtos.UpdateCartItemRequest;
import com.yatc.helloworld.exceptions.CartNotFoundException;
import com.yatc.helloworld.exceptions.ProductNotFoundException;
import com.yatc.helloworld.services.CartService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriComponentsBuilder) {
        CartDto cartDto = cartService.createCart();

        URI uri = uriComponentsBuilder
                .path("/carts/{cartId}")
                .buildAndExpand(cartDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addProductToCart(
            @PathVariable UUID cartId,
            @RequestBody AddToCartRequest request) {
        CartItemDto cartItemDto = cartService.addProductToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable UUID cartId) {
        CartDto cartDto = cartService.getCart(cartId);

        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        CartItemDto cartItemDto = cartService.updateCartItem(cartId, productId, request.getQuantity());

        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartDto> deleteCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId) {
        CartDto cartDto = cartService.deleteCartItem(cartId, productId);

        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<CartDto> clearCart(
            @PathVariable UUID cartId) {
        CartDto cartDto = cartService.clearCart(cartId);

        return ResponseEntity.ok(cartDto);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Product not found in the cart"));
    }
}
