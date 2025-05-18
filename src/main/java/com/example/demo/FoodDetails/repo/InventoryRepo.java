package com.example.demo.FoodDetails.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.FoodDetails.model.Inventory;

public interface InventoryRepo extends JpaRepository<Inventory, String> {
    
}
