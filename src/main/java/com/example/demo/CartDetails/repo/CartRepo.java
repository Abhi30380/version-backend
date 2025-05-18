package com.example.demo.CartDetails.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.CartDetails.model.Cart;

public interface CartRepo extends JpaRepository<Cart, String> {
    Cart findByUsername(String username);
}
