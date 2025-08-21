package com.example.meat_home.service;

import com.example.meat_home.entity.Customer;
import com.example.meat_home.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CustomerRepository customerRepository;

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Activate / Deactivate a customer
    public Customer toggleCustomerStatus(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));

        customer.setIsActive(!customer.getIsActive()); // toggle status
        return customerRepository.save(customer);
    }
}