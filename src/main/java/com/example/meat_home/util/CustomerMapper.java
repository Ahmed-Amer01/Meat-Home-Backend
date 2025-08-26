package com.example.meat_home.util;

import com.example.meat_home.dto.Customer.CustomerDto;
import com.example.meat_home.entity.Address;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.entity.Order;
import com.example.meat_home.repository.AddressRepository;
import com.example.meat_home.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    public CustomerDto toDto(Customer customer) {
        if (customer == null) return null;

        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setPassword(customer.getPassword());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setOrder_id(customer.getOrders()
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList()));

        dto.setAddress_id(customer.getAddresses()
                .stream()
                .map(Address::getId)
                .collect(Collectors.toList()));
        return dto;
    }

    public Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;

        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setPassword(dto.getPassword());
        customer.setDateOfBirth(dto.getDateOfBirth());

        customer.setOrders(
                dto.getOrder_id()
                        .stream()
                        .map(id -> orderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Order not found: " + id)))
                        .collect(Collectors.toList())
        );

        customer.setAddresses(
                dto.getAddress_id()
                        .stream()
                        .map(id -> addressRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Address not found: " + id)))
                        .collect(Collectors.toList())
        );

        return customer;
    }
}
