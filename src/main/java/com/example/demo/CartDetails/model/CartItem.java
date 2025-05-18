package com.example.demo.CartDetails.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.demo.FoodDetails.model.Food;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

    @Id

    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;
    private OffsetDateTime addedAt;
    private OffsetDateTime updatedAt;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;
}
