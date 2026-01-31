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

@RestController
public class FoodController {
    FoodService foodService;

    @Autowired
    UploadFoodImage uploadFoodImage;

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
    public ResponseEntity<String> addProduct(@RequestBody Food food) {
        return foodService.addProduct(food);
    }

    @PostMapping("/upload-image")
    public Map uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String publicId = UUID.randomUUID().toString();
        return uploadFoodImage.UploadFoodImage(file.getBytes(), publicId);
    }
}
