package com.yatc.helloworld.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate date;

    // orphanRemoval = true deletes the row from cart_items table if cart_id is null
    // (when we remove a product from the cart).
    // This is required before we save the cart after removing the product because
    // cart_id in cart_items can't be null
    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        return cartItems
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getCartItem(Long productId) {
        return cartItems
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addProductToCart(Product product) {
        CartItem cartItem = getCartItem(product.getId());

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();

            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);

            cartItems.add(cartItem);
        }

        return cartItem;
    }

    public void removeProductFromCart(Long productId) {
        CartItem cartItem = getCartItem(productId);

        if (cartItem != null) {
            cartItems.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}
