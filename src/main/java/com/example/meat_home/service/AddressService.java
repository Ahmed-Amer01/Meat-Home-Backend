package com.example.meat_home.service;

import com.example.meat_home.dto.Address.AddressDto;
import com.example.meat_home.dto.Address.CreateAddressDto;
import com.example.meat_home.entity.Address;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.repository.AddressRepository;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.util.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerRepository customerRepository;

    /** Get all addresses */
    public List<AddressDto> getAddresses() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Get a single address by ID */
    public AddressDto getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDto)
                .orElse(null);
    }

    /** Create new address */
    public AddressDto createAddress(CreateAddressDto dto) {
        if (dto == null) return null;

        // find the customer
        Customer customer = customerRepository.findById(dto.getCustomer_id()).orElse(null);
        if (customer == null) return null;

        Address address = Address.builder()
                .apartmentNumber(dto.getApartmentNumber())
                .buildingNumber(dto.getBuildingNumber())
                .region(dto.getRegion())
                .stName(dto.getStName())
                .stNumber(dto.getStNumber())
                .customer(customer)
                .build();

        addressRepository.save(address);
        return addressMapper.toDto(address);
    }

    /** Delete address by ID */
    public Boolean deleteAddressById(Long id) {
        if (!addressRepository.existsById(id)) return false;
        addressRepository.deleteById(id);
        return true;
    }

    /** Partial update (PATCH) */
    public AddressDto updateAddressPatch(Long id, CreateAddressDto dto) {
        Address address = addressRepository.findById(id)
                .orElse(null);
        if (dto == null || address == null) return null;

        if (dto.getApartmentNumber() != null) address.setApartmentNumber(dto.getApartmentNumber());
        if (dto.getRegion() != null) address.setRegion(dto.getRegion());
        if (dto.getStNumber() != null) address.setStNumber(dto.getStNumber());
        if (dto.getStName() != null) address.setStName(dto.getStName());
        if (dto.getBuildingNumber() != null) address.setBuildingNumber(dto.getBuildingNumber());


        addressRepository.save(address);
        return addressMapper.toDto(address);
    }
}
