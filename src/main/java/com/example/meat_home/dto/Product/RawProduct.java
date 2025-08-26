package com.example.meat_home.dto.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawProduct {
    private Long id;
    private Double price;
    private String disc;
    private String name;
}
