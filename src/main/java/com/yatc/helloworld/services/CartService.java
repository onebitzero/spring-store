package com.yatc.helloworld.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.yatc.helloworld.dtos.CartDto;
import com.yatc.helloworld.dtos.CartItemDto;
import com.yatc.helloworld.entities.Cart;
import com.yatc.helloworld.entities.CartItem;
import com.yatc.helloworld.entities.Product;
import com.yatc.helloworld.exceptions.CartNotFoundException;
import com.yatc.helloworld.exceptions.ProductNotFoundException;
import com.yatc.helloworld.mappers.CartMapper;
import com.yatc.helloworld.repositories.CartRepository;
import com.yatc.helloworld.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartService {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartDto createCart() {
        Cart cart = new Cart();

        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addProductToCart(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            throw new ProductNotFoundException();
        }

        CartItem cartItem = cart.addProductToCart(product);

        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        CartItem cartItem = cart.getCartItem(productId);

        if (cartItem == null) {
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);

        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto deleteCartItem(UUID cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.removeProductFromCart(productId);

        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartDto clearCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.clearCart();

        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }
}
