package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Users;

public interface UserRepo  extends JpaRepository<Users, String> {

    Users findByUsername(String username);
    
}
