package com.example.meat_home.repository;

import com.example.meat_home.entity.*;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // find by email
    Optional<Customer> findByEmail(String email);

    // check if email exists
    boolean existsByEmail(String email);
}