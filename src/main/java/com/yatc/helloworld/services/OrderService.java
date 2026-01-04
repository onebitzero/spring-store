package com.yatc.helloworld.services;

import com.yatc.helloworld.dtos.OrderDto;
import com.yatc.helloworld.entities.Order;
import com.yatc.helloworld.entities.User;
import com.yatc.helloworld.exceptions.OrderNotFoundException;
import com.yatc.helloworld.mappers.OrderMapper;
import com.yatc.helloworld.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final AuthService authService;

    public List<OrderDto> getAllOrderItems() {
        User user = authService.getCurrentUser();

        List<Order> orders = orderRepository.findAllByCustomerWithItems(user);

        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository
                .findByIdWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);

        User user = authService.getCurrentUser();

        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("You don't have permission to access this order");
        }

        return orderMapper.toDto(order);
    }
}
