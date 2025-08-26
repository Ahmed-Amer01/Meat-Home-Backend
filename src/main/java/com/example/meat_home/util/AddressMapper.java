package com.example.meat_home.util;

import com.example.meat_home.dto.Address.AddressDto;
import com.example.meat_home.entity.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {
    private final CustomerMapper customerMapper;
    public AddressDto toDto(Address address) {
        if (address == null) return null;

        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setApartmentNumber(address.getApartmentNumber());
        dto.setRegion(address.getRegion());
        dto.setStNumber(address.getStNumber());
        dto.setStName(address.getStName());
        dto.setBuildingNumber(address.getBuildingNumber());
        if(address.getCustomer() != null) dto.setCustomer(customerMapper.toDto(address.getCustomer()));

        return dto;
    }
}
