package com.yatc.helloworld.controllers;

import com.yatc.helloworld.dtos.ErrorDto;
import com.yatc.helloworld.dtos.OrderDto;
import com.yatc.helloworld.exceptions.OrderNotFoundException;
import com.yatc.helloworld.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrderItems();
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleOrderNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDto(e.getMessage()));
    }
}
