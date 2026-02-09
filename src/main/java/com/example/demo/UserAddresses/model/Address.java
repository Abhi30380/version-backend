package com.example.demo.UserAddresses.model;

import com.example.demo.common.Status;
import com.example.demo.model.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "address", indexes = { @Index(columnList = "user_id"), @Index(columnList = "status") })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Address {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PUBLISHED;

    @Column(nullable = false)
    private String name;

    @Column(name = "search_address")
    private String searchAddress;

    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(nullable = false)
    private String city;

    private String state;
    private String zipcode;

    @Column(nullable = false)
    private String country;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Users user;
}
