package com.example.demo.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Users;
import com.example.demo.repo.UserRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LoginService {

    UserRepo userRepo;
    BCryptPasswordEncoder encode = new BCryptPasswordEncoder(12);

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService JWTService;

    public LoginService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity<String> Signin(Users user) {
        Users oldUser = userRepo.findByUsername(user.getUsername());
        if (oldUser != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("user already exist");
        }
        user.setPassword(encode.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString());
        user.setCreatedAt(OffsetDateTime.now());
        userRepo.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("login successully");
    }

    public List<Users> getAll() {
        return userRepo.findAll();
    }

    public String login(Users user) {

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return JWTService.generateToken(user.getUsername());
        }
        return "failed";
    }

    public Users getUserDetails(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7).trim();
            username = JWTService.extractUsername(token);
        }

        Users user = userRepo.findByUsername(username);
        return user;
    }

    public Map<String, Object> updateUserDetails(Users user_detials) {
        System.out.println("this is updated user_details"+user_detials);
        userRepo.save(user_detials);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "user details updated successfully");
        response.put("status_code", 200);
        return response;
    }
}
