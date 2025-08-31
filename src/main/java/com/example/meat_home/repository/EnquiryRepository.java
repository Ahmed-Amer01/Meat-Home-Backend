package com.example.meat_home.repository;

import com.example.meat_home.entity.Enquiry;
import com.example.meat_home.entity.EnquiryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {
    List<Enquiry> findByCustomerId(int customerId);
}


