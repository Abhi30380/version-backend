package com.example.demo.FoodDetails.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Inventory {
    
    @Id
    private String id;
    private int availableQuantity;
}
