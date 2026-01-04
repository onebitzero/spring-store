package com.yatc.helloworld.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDto {
    private Long id;

    private String name;

    private BigDecimal price;

    private Byte categoryId;
}
