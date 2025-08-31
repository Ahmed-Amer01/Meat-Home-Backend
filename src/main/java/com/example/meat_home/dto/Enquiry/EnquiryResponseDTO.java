package com.example.meat_home.dto.Enquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnquiryResponseDTO {
    private Integer id;
    private Integer enquiryId;
    private String responderType;  // "CUSTOMER" or "STAFF"
    private Integer responderId;   // only ID, not full Staff/Customer
    private String response;
    private LocalDateTime createdAt;
}
