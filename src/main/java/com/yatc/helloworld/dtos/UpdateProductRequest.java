package com.yatc.helloworld.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;

    private BigDecimal price;

    private Byte categoryId;
}
