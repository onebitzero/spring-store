package com.yatc.helloworld.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yatc.helloworld.dtos.CartDto;
import com.yatc.helloworld.dtos.CartItemDto;
import com.yatc.helloworld.entities.Cart;
import com.yatc.helloworld.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {
    // If name of variable is also "items" in Cart, as it is in CartDto, we can
    // avoid using @Mapping
    @Mapping(target = "items", source = "cartItems")
    CartDto toDto(Cart cart);

    // @Mapping(target = "totalPrice", expression =
    // "java(cartItem.getTotalPrice())")
    // We can avoid using @Mapping because mapstruct maps totalPrice using
    // getTotalPrice method
    CartItemDto toDto(CartItem cartItem);
}
