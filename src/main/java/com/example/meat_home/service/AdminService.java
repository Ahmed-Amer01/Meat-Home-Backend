package com.example.meat_home.service;

import com.example.meat_home.entity.Customer;
import com.example.meat_home.entity.Staff;
import com.example.meat_home.repository.CustomerRepository;
import com.example.meat_home.repository.StaffRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Toggle status for customer or staff (Delivery, CallCenter)
    public Object toggleUserStatus(Long id, String type) {
        if ("customer".equalsIgnoreCase(type)) {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
            customer.setIsActive(!customer.getIsActive());
            return customerRepository.save(customer);

        } else if ("staff".equalsIgnoreCase(type)) {
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));

            // Prevent disabling ADMIN accounts unless it’s intentional
            if (staff.getRole() == Staff.Role.ADMIN) {
                throw new IllegalArgumentException("Cannot deactivate ADMIN users!");
            }

            staff.setIsActive(!staff.getIsActive());
            return staffRepository.save(staff);
        }

        throw new IllegalArgumentException("Invalid type: must be 'customer' or 'staff'");
    }
}