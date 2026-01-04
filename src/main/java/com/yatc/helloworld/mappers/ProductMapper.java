package com.yatc.helloworld.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.yatc.helloworld.dtos.CreateProductRequest;
import com.yatc.helloworld.dtos.ProductDto;
import com.yatc.helloworld.dtos.UpdateProductRequest;
import com.yatc.helloworld.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    Product toEntity(CreateProductRequest request);

    void update(UpdateProductRequest request, @MappingTarget Product product);
}
