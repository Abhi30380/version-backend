package com.example.demo.DocumentDetails.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.UserAddresses.model.Address;
import com.example.demo.common.DeliveryStatus;
import com.example.demo.model.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "document", indexes = { @Index(columnList = "user_id"), @Index(columnList = "delivery_status") })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Document {

    @Id
    private String id;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DocumentItem> documentItems = new ArrayList<>();

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /** Delivery address (default: user's primary address at checkout). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
}
