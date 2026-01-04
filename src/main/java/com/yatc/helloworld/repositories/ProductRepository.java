package com.yatc.helloworld.repositories;

import com.yatc.helloworld.entities.Product;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findAllByCategoryId(int categoryId);
}
