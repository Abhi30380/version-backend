package com.example.demo.CartDetails.model;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.DocumentDetails.model.Document;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    private String id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CartItem> cartItems = new ArrayList<>();
    private int totalAmount;
    private int totalItems;
    private int totalQuantity;
    private String username;
    private String status;

    /** Link to Document (order) after checkout. Once set, cart is "used" and user gets a new cart. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_link_to")
    private Document cartLinkTo;

    public void addCartItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);
    }
}
