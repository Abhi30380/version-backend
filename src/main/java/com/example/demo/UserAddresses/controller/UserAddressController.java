package com.example.demo.UserAddresses.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.UserAddresses.model.Address;
import com.example.demo.common.Status;
import com.example.demo.UserAddresses.service.UserAddressService;
import com.example.demo.model.Users;
import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;
    private final LoginService loginService;

    private String currentUserId(HttpServletRequest request) {
        Users user = loginService.getUserDetails(request);
        if (user == null) throw new IllegalStateException("Unauthorized");
        return user.getId();
    }

    @GetMapping
    public ResponseEntity<List<Address>> getMyAddresses(
            @RequestParam(defaultValue = "PUBLISHED") Status status,
            HttpServletRequest request) {
        String userId = currentUserId(request);
        return ResponseEntity.ok(userAddressService.getAddressesByUserId(userId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(
            @PathVariable String id,
            @RequestParam(required = false) Status status,
            HttpServletRequest request) {
        String userId = currentUserId(request);
        Address address = userAddressService.getAddressByIdAndUserId(id, userId, status);
        if (address == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address, HttpServletRequest request) {
        String userId = currentUserId(request);
        Address saved = userAddressService.addAddress(userId, address);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable String id,
            @RequestBody Address address,
            HttpServletRequest request) {
        String userId = currentUserId(request);
        try {
            Address updated = userAddressService.updateAddress(id, userId, address);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Soft delete: sets status to ARCHIVE. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String id, HttpServletRequest request) {
        String userId = currentUserId(request);
        try {
            userAddressService.deleteAddress(id, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Restore: set status from ARCHIVE to PUBLISHED. */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Address> restoreAddress(@PathVariable String id, HttpServletRequest request) {
        String userId = currentUserId(request);
        try {
            Address updated = userAddressService.restoreAddress(id, userId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<Address> setPrimary(@PathVariable String id, HttpServletRequest request) {
        String userId = currentUserId(request);
        try {
            Address updated = userAddressService.setPrimaryAddress(id, userId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
