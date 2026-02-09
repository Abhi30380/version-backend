package com.example.demo.DocumentDetails.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DocumentDetails.model.DocumentItem;

public interface DocumentItemRepo extends JpaRepository<DocumentItem, String> {

    List<DocumentItem> findByDocument_Id(String documentId);
}
