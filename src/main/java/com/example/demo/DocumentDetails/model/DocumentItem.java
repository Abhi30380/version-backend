package com.example.demo.DocumentDetails.model;

import com.example.demo.FoodDetails.model.Food;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document_item", indexes = {
    @Index(name = "idx_document_item_seller", columnList = "seller_id")
})
public class DocumentItem {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(name = "seller_id")
    private String sellerId;

    @Column(nullable = false)
    private int quantity;

    /** Price per unit at time of order (for order history). */
    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    @JsonIgnore
    private Document document;
}
