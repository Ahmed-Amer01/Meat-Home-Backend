package com.example.meat_home.service;

import com.example.meat_home.dto.Customer.CustomerDto;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.util.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepo;
    private final CustomerMapper customerMapper;

    /** Get all customers as DTOs */
    public List<CustomerDto> getCustomers() {
        return customerRepo.findAll()
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Get a single customer by ID */
    public CustomerDto getCustomer(Long id) {
        return customerRepo.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    /** Create a new active customer */
    public CustomerDto createCustomer(CustomerDto dto) {
        if (dto == null) return null;

        Customer customer = customerMapper.toEntity(dto);
        customer.setIsActive(true);
        Customer saved = customerRepo.save(customer);
        return customerMapper.toDto(saved);
    }

    /** Delete customer by ID */
    public boolean deleteCustomerById(Long id) {
        if (!customerRepo.existsById(id)) return false;
        customerRepo.deleteById(id);
        return true;
    }

    /** Delete customer by DTO */
    public boolean deleteCustomer(CustomerDto dto) {
        if (dto == null || !customerRepo.existsById(dto.getId())) return false;
        Customer customer = customerMapper.toEntity(dto);
        customerRepo.delete(customer);
        return true;
    }

    /** Partial update (PATCH) */
    public CustomerDto updateCustomerPatch(Long id, CustomerDto dto) {
        Customer customer = customerRepo.findById(id)
                .orElse(null);
        if (dto == null || customer == null) return null;

        if (dto.getName() != null) customer.setName(dto.getName());
        if (dto.getEmail() != null) customer.setEmail(dto.getEmail());
        if (dto.getPhone() != null) customer.setPhone(dto.getPhone());
        if (dto.getPassword() != null) customer.setPassword(dto.getPassword());
        if (dto.getDateOfBirth() != null) customer.setDateOfBirth(dto.getDateOfBirth());

        Customer savedCustomer = customerRepo.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

}
