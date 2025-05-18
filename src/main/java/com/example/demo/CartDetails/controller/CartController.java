package com.example.demo.CartDetails.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.CartDetails.model.Cart;
import com.example.demo.CartDetails.model.CartItem;
import com.example.demo.CartDetails.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CartController {

    CartService cartService;
    
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping("/get-cart-details")
    public Cart hello(HttpServletRequest request) {
        return cartService.getUserCart(request);
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpServletRequest request, @RequestBody CartItem cartItem) {
        return cartService.addToCart(request, cartItem);
    }
}
