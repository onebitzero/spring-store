package com.yatc.helloworld.mappers;

import com.yatc.helloworld.dtos.OrderDto;
import com.yatc.helloworld.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
