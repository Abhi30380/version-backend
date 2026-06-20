package com.example.demo.FoodDetails.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.FoodDetails.model.Food;
import com.example.demo.FoodDetails.service.FoodService;
import com.example.demo.FoodDetails.service.UploadFoodImage;
import com.example.demo.model.Users;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.JWTService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class FoodController {
    FoodService foodService;

    @Autowired
    UploadFoodImage uploadFoodImage;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserRepo userRepo;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/get-all-products")
    public List<Food> getAllProducts() {
        return foodService.getAllProducts();
    }

    @PostMapping("/search/products")
    public List<Food> searchProduct(@RequestBody Map<String, String> request) {
        return foodService.searchProduct(request);
    }

    @GetMapping("/search/suggestions")
    public List<String> getSuggestions(@RequestParam String q) {
        return foodService.getSuggestions(q);
    }

    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestBody Food food, HttpServletRequest request) {
        String username = jwtService.getUsername(request);
        Users user = userRepo.findByUsername(username);
        return foodService.addProduct(food, user);
    }

    @PostMapping("/upload-image")
    public Map uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String publicId = UUID.randomUUID().toString();
        return uploadFoodImage.UploadFoodImage(file.getBytes(), publicId);
    }
}
