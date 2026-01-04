package com.yatc.helloworld.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "Enter a quantity.")
    @Min(value = 1, message = "Quantity must be greater than 0.")
    private Integer quantity;
}
