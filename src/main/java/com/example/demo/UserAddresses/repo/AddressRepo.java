package com.example.demo.UserAddresses.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.common.Status;
import com.example.demo.UserAddresses.model.Address;

public interface AddressRepo extends JpaRepository<Address, String> {

    List<Address> findByUser_IdAndStatusOrderByIsPrimaryDescIdAsc(String userId, Status status);

    Optional<Address> findByIdAndUser_Id(String id, String userId);

    boolean existsByIdAndUser_Id(String id, String userId);

    @Modifying
    @Query("UPDATE Address a SET a.isPrimary = false WHERE a.user.id = :userId AND a.status = :published")
    void clearPrimaryByUserId(@Param("userId") String userId, @Param("published") Status published);
}
