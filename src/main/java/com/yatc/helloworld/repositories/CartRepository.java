package com.yatc.helloworld.repositories;

import com.yatc.helloworld.entities.Cart;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, UUID> {

}