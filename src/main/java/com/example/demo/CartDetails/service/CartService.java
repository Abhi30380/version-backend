package com.example.demo.CartDetails.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.CartDetails.model.Cart;
import com.example.demo.CartDetails.model.CartItem;
import com.example.demo.CartDetails.repo.CartItemRepo;
import com.example.demo.CartDetails.repo.CartRepo;
import com.example.demo.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CartService {
    CartRepo cartRepo;

    @Autowired
    JWTService JWTService;

    @Autowired
    CartItemRepo cartItemRepo;

    public CartService(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    public Cart getUserCart(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim();
            username = JWTService.extractUsername(token);
        }
        Cart cart = cartRepo.findByUsername(username);
        if(cart == null) {
            return createCart(request, username);
        }
        return cart;
    }

    public Cart createCart(HttpServletRequest request, String username) {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUsername(username);
        cartRepo.save(cart);
        return cart;
    }

    public String addToCart(HttpServletRequest request, CartItem cartItem) {
        String username = JWTService.getUsername(request);
        Cart cart = cartRepo.findByUsername(username);
        if (cart == null) {
            cart = createCart(request, username);
        }
        CartItem updatedCartItem = cartItem.getId() != null ? cartItemRepo.findById(cartItem.getId()).orElse(null) : null;
        if (updatedCartItem != null) {
            updatedCartItem.setQuantity(updatedCartItem.getQuantity() + 1);
            cart.setTotalAmount(cart.getTotalAmount() + cartItem.getFood().getPrice());
            cart.setTotalQuantity(cart.getTotalQuantity() + 1);
            cartItemRepo.save(updatedCartItem);
            return "add to cart successfully";
        }
        cartItem.setQuantity(1);
        cart.setTotalItems(cart.getTotalItems() + 1);
        cart.setTotalAmount(cart.getTotalAmount() + cartItem.getFood().getPrice());
        cart.setTotalQuantity(cart.getTotalQuantity() + 1);
        cart.addCartItem(cartItem);
        cartRepo.save(cart);
        return "add to cart successfully";
    }
    public String removeCartItem(HttpServletRequest request, CartItem cartItem) {
        String username = JWTService.getUsername(request);
        Cart cart = cartRepo.findByUsername(username);

        CartItem updatedCartItem = cartItemRepo.findById(cartItem.getId()).orElse(null);
        if (updatedCartItem != null) {
            if (updatedCartItem.getQuantity() > 1) {
                updatedCartItem.setQuantity(updatedCartItem.getQuantity() - 1);
                cartItemRepo.save(updatedCartItem);
                return "remove from cart successfully";
            } else {
                cartItemRepo.delete(updatedCartItem);
                cart.getCartItems().remove(updatedCartItem);
                cartRepo.save(cart);
                return "remove from cart successfully";
            }
        }

        return "remove from cart successfully";
    }
}
