package com.example.demo.FoodDetails.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Food {
    @Id
    private String id;
    private String name;
    private String description;
    private int price;
    private String currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    private List<String> images;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private String createdByName;
    private String updatedByName;
    private String createdBy;
    private String updatedBy;
}
