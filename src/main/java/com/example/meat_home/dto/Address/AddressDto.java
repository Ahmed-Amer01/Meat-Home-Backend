package com.example.meat_home.dto.Address;

import com.example.meat_home.dto.Customer.CustomerDto;
import com.example.meat_home.entity.RegionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private Integer apartmentNumber;
    private RegionEnum region;
    private Integer stNumber;
    private String stName;
    private Integer buildingNumber;
    private CustomerDto customer;
}
