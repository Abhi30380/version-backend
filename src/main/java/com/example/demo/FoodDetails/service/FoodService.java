package com.example.demo.FoodDetails.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.FoodDetails.model.Food;
import com.example.demo.FoodDetails.model.Inventory;
import com.example.demo.FoodDetails.repo.FoodRepo;

@Service
public class FoodService {
    FoodRepo foodRepo;

    public FoodService(FoodRepo foodRepo) {
        this.foodRepo = foodRepo;
    }

    public List<Food> getAllProducts() {
        return foodRepo.findAll();
    }

    public String addProduct(Food food) {
        food.setId(UUID.randomUUID().toString());
        OffsetDateTime now = OffsetDateTime.now();

        food.setCreatedAt(now);
        food.setUpdatedAt(now);
        Inventory inventory = food.getInventory();
        if (inventory != null) {
            inventory.setId(UUID.randomUUID().toString());
        }
        foodRepo.save(food);
        return "Product added successfully";
    }
}
