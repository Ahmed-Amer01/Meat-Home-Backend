package com.example.meat_home.controller;

import com.example.meat_home.dto.Enquiry.*;
import com.example.meat_home.entity.Enquiry;
import com.example.meat_home.entity.EnquiryResponse;
import com.example.meat_home.service.EnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/enquiries")
@RequiredArgsConstructor
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{customerId}")
    public ResponseEntity<EnquiryDTO> createEnquiry(
            @PathVariable Long customerId,
            @Valid @RequestBody EnquiryRequest request) {
        Enquiry enquiry = enquiryService.createEnquiry(customerId, request.getMessage());
        return ResponseEntity.ok(EnquiryMapper.toDTO(enquiry));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CALLCENTER')")
    @GetMapping
    public ResponseEntity<List<EnquiryDTO>> getAllEnquiries() {
        List<EnquiryDTO> enquiries = enquiryService.getAllEnquiries()
                .stream()
                .map(EnquiryMapper::toDTO)
                .toList();
        return ResponseEntity.ok(enquiries);
    }

    @PostMapping("/{enquiryId}/respond")
    @PreAuthorize("hasRole('CALLCENTER')")
    public ResponseEntity<EnquiryResponseDTO> respondToEnquiry(
            @PathVariable Long enquiryId,
            @Valid @RequestBody EnquiryResponseRequest request) {  // use request body
        EnquiryResponse response = enquiryService.respondToEnquiry(
                enquiryId,
                request.getStaffId(),
                request.getResponseMessage()
        );
        return ResponseEntity.ok(EnquiryMapper.toResponseDTO(response));
    }

    @PostMapping("/{enquiryId}/close")
    @PreAuthorize("hasRole('CALLCENTER')")
    public ResponseEntity<Void> closeEnquiry(@PathVariable Long enquiryId) {
        enquiryService.closeEnquiry(enquiryId);
        return ResponseEntity.noContent().build();
    }
}
