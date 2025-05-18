package com.example.demo.CartDetails.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.CartDetails.model.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, UUID> {
    
}
