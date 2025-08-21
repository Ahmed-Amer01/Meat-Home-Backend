package com.example.meat_home.service;

import com.example.meat_home.entity.Staff;
import com.example.meat_home.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepo;

    public Staff findById(Long id) {
        return staffRepo.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
    }
}