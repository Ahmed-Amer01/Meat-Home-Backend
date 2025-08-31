package com.example.meat_home.dto.Enquiry;

import com.example.meat_home.entity.Enquiry;
import com.example.meat_home.entity.EnquiryResponse;

public class EnquiryMapper {

    public static EnquiryDTO toDTO(Enquiry enquiry) {
        return EnquiryDTO.builder()
                .id(enquiry.getId())
                .message(enquiry.getMessage())
                .status(String.valueOf(enquiry.getStatus()))
                .createdAt(enquiry.getCreatedAt())
                .updatedAt(enquiry.getUpdatedAt())
                .customerId(enquiry.getCustomer().getId())
                .build();
    }

    public static EnquiryResponseDTO toResponseDTO(EnquiryResponse response) {
        return EnquiryResponseDTO.builder()
                .id(response.getId().intValue())
                .enquiryId(response.getEnquiry().getId().intValue())
                .responderType("STAFF") // For now, only staff responses exist
                .responderId(response.getResponder().getId().intValue())
                .response(response.getResponseMessage())
                .createdAt(response.getRespondedAt())
                .build();
    }
}
