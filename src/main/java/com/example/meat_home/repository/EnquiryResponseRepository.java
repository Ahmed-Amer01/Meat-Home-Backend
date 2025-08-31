package com.example.meat_home.repository;

import com.example.meat_home.entity.EnquiryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryResponseRepository extends JpaRepository<EnquiryResponse, Integer> {
    List<EnquiryResponse> findByEnquiryId(int enquiryId);
}