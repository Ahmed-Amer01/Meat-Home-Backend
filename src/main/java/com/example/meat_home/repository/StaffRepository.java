package com.example.meat_home.repository;

import com.example.meat_home.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // find by email
    Optional<Staff> findByEmail(String email);

    // check if email exists
    boolean existsByEmail(String email);
}