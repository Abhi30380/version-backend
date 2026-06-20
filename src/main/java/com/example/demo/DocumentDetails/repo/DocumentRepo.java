package com.example.demo.DocumentDetails.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.DocumentDetails.model.Document;
import com.example.demo.common.DeliveryStatus;

public interface DocumentRepo extends JpaRepository<Document, String> {

    List<Document> findByUser_IdOrderByCreatedAtDesc(String userId);

    List<Document> findByUser_IdAndDeliveryStatusOrderByCreatedAtDesc(String userId, DeliveryStatus deliveryStatus);

    @Query("SELECT DISTINCT d FROM Document d JOIN d.documentItems di " +
           "WHERE di.sellerId = :sellerId AND d.deliveryStatus <> 'PENDING' " +
           "ORDER BY d.createdAt DESC")
    Page<Document> findSellerOrders(@Param("sellerId") String sellerId, Pageable pageable);

    @Query("SELECT DISTINCT d FROM Document d JOIN d.documentItems di " +
           "WHERE di.sellerId = :sellerId AND d.deliveryStatus = :status " +
           "ORDER BY d.createdAt DESC")
    Page<Document> findSellerOrdersByStatus(
            @Param("sellerId") String sellerId,
            @Param("status") DeliveryStatus status,
            Pageable pageable);
}
