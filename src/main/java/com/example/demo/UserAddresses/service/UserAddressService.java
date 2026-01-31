package com.example.demo.UserAddresses.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.Status;
import com.example.demo.UserAddresses.model.Address;
import com.example.demo.UserAddresses.repo.AddressRepo;
import com.example.demo.model.Users;
import com.example.demo.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final AddressRepo addressRepo;
    private final UserRepo userRepo;

    public List<Address> getAddressesByUserId(String userId, Status status) {
        return addressRepo.findByUser_IdAndStatusOrderByIsPrimaryDescIdAsc(userId, status);
    }

    public Address getAddressByIdAndUserId(String addressId, String userId, Status status) {
        return addressRepo.findByIdAndUser_Id(addressId, userId)
                .filter(a -> status == null || a.getStatus() == status)
                .orElse(null);
    }

    @Transactional
    public Address addAddress(String userId, Address address) {
        Users user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        address.setUser(user);
        address.setId(UUID.randomUUID().toString());
        address.setStatus(Status.PUBLISHED);
        if (Boolean.TRUE.equals(address.getIsPrimary())) {
            addressRepo.clearPrimaryByUserId(userId, Status.PUBLISHED);
        }
        return addressRepo.save(address);
    }

    @Transactional
    public Address updateAddress(String addressId, String userId, Address updates) {
        Address existing = addressRepo.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        if (existing.getStatus() != Status.PUBLISHED) {
            throw new IllegalArgumentException("Cannot update archived address");
        }
        existing.setName(updates.getName() != null ? updates.getName() : existing.getName());
        existing.setSearchAddress(updates.getSearchAddress() != null ? updates.getSearchAddress() : existing.getSearchAddress());
        existing.setAddressLine1(updates.getAddressLine1() != null ? updates.getAddressLine1() : existing.getAddressLine1());
        existing.setAddressLine2(updates.getAddressLine2() != null ? updates.getAddressLine2() : existing.getAddressLine2());
        existing.setCity(updates.getCity() != null ? updates.getCity() : existing.getCity());
        existing.setState(updates.getState() != null ? updates.getState() : existing.getState());
        existing.setZipcode(updates.getZipcode() != null ? updates.getZipcode() : existing.getZipcode());
        existing.setCountry(updates.getCountry() != null ? updates.getCountry() : existing.getCountry());
        if (Boolean.TRUE.equals(updates.getIsPrimary())) {
            addressRepo.clearPrimaryByUserId(userId, Status.PUBLISHED);
            existing.setIsPrimary(true);
        } else if (updates.getIsPrimary() != null) {
            existing.setIsPrimary(updates.getIsPrimary());
        }
        return addressRepo.save(existing);
    }

    /** Soft delete: set status to ARCHIVE. */
    @Transactional
    public void deleteAddress(String addressId, String userId) {
        Address address = addressRepo.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        address.setStatus(Status.ARCHIVE);
        address.setIsPrimary(false);
        addressRepo.save(address);
    }

    /** Restore: set status from ARCHIVE to PUBLISHED. */
    @Transactional
    public Address restoreAddress(String addressId, String userId) {
        Address address = addressRepo.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        if (address.getStatus() != Status.ARCHIVE) {
            throw new IllegalArgumentException("Address is not archived");
        }
        address.setStatus(Status.PUBLISHED);
        return addressRepo.save(address);
    }

    @Transactional
    public Address setPrimaryAddress(String addressId, String userId) {
        Address address = addressRepo.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        if (address.getStatus() != Status.PUBLISHED) {
            throw new IllegalArgumentException("Cannot set archived address as primary");
        }
        addressRepo.clearPrimaryByUserId(userId, Status.PUBLISHED);
        address.setIsPrimary(true);
        return addressRepo.save(address);
    }
}
