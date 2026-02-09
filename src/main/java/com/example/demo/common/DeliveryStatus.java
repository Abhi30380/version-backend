package com.example.demo.common;

/**
 * Shared delivery/order status for documents (orders) or any deliverable entity.
 */
public enum DeliveryStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    IN_TRANSIT,
    DELIVERED,
    DELAYED,
    FAILED,
    CANCELLED,
    PROCESSING
}
