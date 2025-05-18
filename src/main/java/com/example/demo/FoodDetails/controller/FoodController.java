package com.example.demo.FoodDetails.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.FoodDetails.model.Food;
import com.example.demo.FoodDetails.service.FoodService;

@RestController
public class FoodController {
    FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }
    @GetMapping("/get-all-products")
    public List<Food> getAllProducts() {
        return foodService.getAllProducts();
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestBody Food food) {
        return foodService.addProduct(food);
    }
}
