package com.example.meat_home.dto.Enquiry;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class EnquiryResponseRequest {
    @NotNull
    private Long staffId;

    @NotNull
    private String responseMessage;
}
