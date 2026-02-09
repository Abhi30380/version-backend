package com.example.demo.DocumentDetails.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DocumentDetails.model.Document;
import com.example.demo.DocumentDetails.service.DocumentService;
import com.example.demo.common.DeliveryStatus;
import com.example.demo.model.Users;
import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final LoginService loginService;

    private String currentUserId(HttpServletRequest request) {
        Users user = loginService.getUserDetails(request);
        if (user == null) throw new IllegalStateException("Unauthorized");
        return user.getId();
    }

    /** Order history: list all documents (orders) for current user. */
    @GetMapping
    public ResponseEntity<List<Document>> getOrderHistory(
            @RequestParam(required = false) DeliveryStatus status,
            HttpServletRequest request) {
        String userId = currentUserId(request);
        List<Document> list = status == null
                ? documentService.getOrderHistoryByUserId(userId)
                : documentService.getOrderHistoryByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable String id, HttpServletRequest request) {
        String userId = currentUserId(request);
        Document document = documentService.getDocumentByIdAndUserId(id, userId);
        if (document == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(document);
    }

    /** Checkout: create a new document (order) from current user's cart. Cart is not modified. */
    @PostMapping("/checkout")
    public ResponseEntity<Document> checkout(HttpServletRequest request) {
        String userId = currentUserId(request);
        try {
            Document document = documentService.createFromCart(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(document);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /** Update an existing document (order) from current user's cart. Only when document is PENDING. */
    @PutMapping("/{id}/from-cart")
    public ResponseEntity<Document> updateFromCart(@PathVariable String id, HttpServletRequest request) {
        String userId = currentUserId(request);
        try {
            Document document = documentService.updateFromCart(id, userId);
            return ResponseEntity.ok(document);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /** Update delivery status of an order. */
    @PatchMapping("/{id}/delivery-status")
    public ResponseEntity<?> updateDeliveryStatus(
            @PathVariable String id,
            @RequestBody DeliveryStatusPayload payload,
            HttpServletRequest request) {
        String userId = currentUserId(request);
        if (payload == null || payload.getDeliveryStatus() == null) {
            return ResponseEntity.badRequest().body("deliveryStatus is required");
        }
        try {
            Document updated = documentService.updateDeliveryStatus(id, userId, payload.getDeliveryStatus());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Database does not allow this delivery status. Run scripts/fix-delivery-status-constraint.sql on your PostgreSQL database.");
        }
    }

    public static class DeliveryStatusPayload {
        private DeliveryStatus deliveryStatus;

        public DeliveryStatus getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }
    }
}
