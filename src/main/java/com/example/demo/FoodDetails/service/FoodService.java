package com.example.demo.FoodDetails.service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.FoodDetails.model.Food;
import com.example.demo.FoodDetails.model.Inventory;
import com.example.demo.FoodDetails.repo.FoodRepo;

@Service
public class FoodService {
    FoodRepo foodRepo;
    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    public FoodService(FoodRepo foodRepo) {
        this.foodRepo = foodRepo;
    }

    public List<Food> getAllProducts() {
        return foodRepo.findAll();
    }

    public ResponseEntity<String> addProduct(Food food) {
        food.setId(UUID.randomUUID().toString());
        OffsetDateTime now = OffsetDateTime.now();

        food.setCreatedAt(now);
        food.setUpdatedAt(now);
        Inventory inventory = food.getInventory();
        if (inventory != null) {
            inventory.setId(UUID.randomUUID().toString());
        }
        foodRepo.save(food);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Product added successfully");
    }

    public List<Food> searchProduct(Map<String, String> request) {
        String searchTerm = request.get("searchTerm");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return foodRepo.findAll();
        }
        return fuzzySearch(searchTerm);
    }

    public List<Food> findByName(String searchTerm) {
        String term = searchTerm.toLowerCase();
        return foodRepo.findByNameContainingIgnoreCase(term);

    }

    public List<Food> fuzzySearch(String keyword) {
        List<Food> foods = foodRepo.findByNameContainingIgnoreCase(
                keyword.substring(0, 1) // light pre-filter
        );

        return foods.stream()
                .sorted(Comparator.comparingInt(
                        food -> levenshtein.apply(
                                keyword.toLowerCase(),
                                food.getName().toLowerCase()
                        )
                ))
                .collect(Collectors.toList());
    }

    public List<String> getSuggestions(String input) {

        if (input == null || input.trim().isEmpty()) {
            return List.of();
        }

        final String query = input.trim().toLowerCase();

        List<Food> candidates =
                foodRepo.findTop10ByNameStartingWithIgnoreCase(query);

        if (candidates.isEmpty()) {
            String prefix = query.length() >= 2
                    ? query.substring(0, 2)
                    : query;

            candidates = foodRepo.findByNameContainingIgnoreCase(prefix);
        }

        return candidates.stream()
                .map(food -> Map.entry(
                        food.getName(),
                        levenshtein.apply(query, food.getName().toLowerCase())
                ))
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .distinct()
                .limit(8)
                .collect(Collectors.toList());
    }


}
