package com.example.demo.FoodDetails.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.FoodDetails.model.Food;

public interface FoodRepo extends JpaRepository<Food, String> {
    
}
