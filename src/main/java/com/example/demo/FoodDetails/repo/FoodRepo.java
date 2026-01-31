package com.example.demo.FoodDetails.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.FoodDetails.model.Food;

public interface FoodRepo extends JpaRepository<Food, String> {
    List<Food> findByNameContainingIgnoreCase(String name);
    List<Food> findTop10ByNameStartingWithIgnoreCase(String name); 
}
