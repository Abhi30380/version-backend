package com.example.demo.DocumentDetails.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.CartDetails.model.Cart;
import com.example.demo.CartDetails.model.CartItem;
import com.example.demo.CartDetails.repo.CartRepo;
import com.example.demo.DocumentDetails.model.Document;
import com.example.demo.DocumentDetails.model.DocumentItem;
import com.example.demo.DocumentDetails.repo.DocumentItemRepo;
import com.example.demo.DocumentDetails.repo.DocumentRepo;
import com.example.demo.UserAddresses.model.Address;
import com.example.demo.UserAddresses.repo.AddressRepo;
import com.example.demo.common.DeliveryStatus;
import com.example.demo.common.Status;
import com.example.demo.model.Users;
import com.example.demo.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;
    private final DocumentItemRepo documentItemRepo;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final AddressRepo addressRepo;

    public List<Document> getOrderHistoryByUserId(String userId) {
        return documentRepo.findByUser_IdOrderByCreatedAtDesc(userId);
    }

    public List<Document> getOrderHistoryByUserIdAndStatus(String userId, DeliveryStatus status) {
        return documentRepo.findByUser_IdAndDeliveryStatusOrderByCreatedAtDesc(userId, status);
    }

    public Document getDocumentByIdAndUserId(String documentId, String userId) {
        return documentRepo.findById(documentId)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElse(null);
    }

    /**
     * Checkout: create a new Document (order) from the current user's cart.
     * Cart is not modified; document is independent.
     */
    @Transactional
    public Document createFromCart(String userId) {
        Users user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart cart = cartRepo.findByUsername(user.getUsername());
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Get user's primary address (if any)
        Address primaryAddress = addressRepo.findByUser_IdAndIsPrimaryTrueAndStatus(userId, Status.PUBLISHED)
                .orElse(null);

        Document document = new Document();
        document.setId(UUID.randomUUID().toString());
        document.setUser(user);
        document.setAddress(primaryAddress);
        document.setDeliveryStatus(DeliveryStatus.PENDING);
        document.setCreatedAt(OffsetDateTime.now());
        document.setTotalAmount(0);
        document.setTotalQuantity(0);

        document = documentRepo.save(document);

        int totalAmount = 0;
        int totalQuantity = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            DocumentItem item = new DocumentItem();
            item.setId(UUID.randomUUID().toString());
            item.setDocument(document);
            item.setFood(cartItem.getFood());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getFood().getPrice());
            documentItemRepo.save(item);
            document.getDocumentItems().add(item);
            totalAmount += item.getUnitPrice() * item.getQuantity();
            totalQuantity += item.getQuantity();
        }

        document.setTotalAmount(totalAmount);
        document.setTotalQuantity(totalQuantity);
        document = documentRepo.save(document);

        // Link cart to document (unlink / new cart will be handled by place-order API later)
        cart.setCartLinkTo(document);
        cartRepo.save(cart);

        return document;
    }

    /**
     * Update an existing document (order) with the current user's cart.
     * Only allowed when document is PENDING; cart is not modified.
     */
    @Transactional
    public Document updateFromCart(String documentId, String userId) {
        Document document = documentRepo.findById(documentId)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        if (document.getDeliveryStatus() != DeliveryStatus.PENDING) {
            throw new IllegalArgumentException("Can only update document when delivery status is PENDING");
        }

        Users user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart cart = cartRepo.findByUsername(user.getUsername());
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        var existingItems = documentItemRepo.findByDocument_Id(documentId);
        documentItemRepo.deleteAll(existingItems);
        document.getDocumentItems().clear();

        int totalAmount = 0;
        int totalQuantity = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            DocumentItem item = new DocumentItem();
            item.setId(UUID.randomUUID().toString());
            item.setDocument(document);
            item.setFood(cartItem.getFood());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getFood().getPrice());
            documentItemRepo.save(item);
            document.getDocumentItems().add(item);
            totalAmount += item.getUnitPrice() * item.getQuantity();
            totalQuantity += item.getQuantity();
        }

        document.setTotalAmount(totalAmount);
        document.setTotalQuantity(totalQuantity);
        document = documentRepo.save(document);

        // Link cart to document (unlink / new cart will be handled by place-order API later)
        cart.setCartLinkTo(document);
        cartRepo.save(cart);

        return document;
    }

    @Transactional
    public Document updateDeliveryStatus(String documentId, String userId, DeliveryStatus status) {
        Document document = documentRepo.findById(documentId)
                .filter(d -> d.getUser().getId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        document.setDeliveryStatus(status);

        Users user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart cart = cartRepo.findByUsername(user.getUsername());
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found");
        }
        cart.setCartLinkTo(null);
        cartRepo.save(cart);
        return documentRepo.save(document);
    }
}
