package com.example.demo.DocumentDetails.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DocumentDetails.model.Document;
import com.example.demo.common.DeliveryStatus;

public interface DocumentRepo extends JpaRepository<Document, String> {

    List<Document> findByUser_IdOrderByCreatedAtDesc(String userId);

    List<Document> findByUser_IdAndDeliveryStatusOrderByCreatedAtDesc(String userId, DeliveryStatus deliveryStatus);
}
