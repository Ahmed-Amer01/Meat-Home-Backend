package com.example.meat_home.repository;

import com.example.meat_home.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // find by email
    Optional<Customer> findByEmail(String email);

    // check if email exists
    boolean existsByEmail(String email);
}
