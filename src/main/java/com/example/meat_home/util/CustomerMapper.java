package com.example.meat_home.util;

import com.example.meat_home.dto.Customer.CustomerDto;
import com.example.meat_home.entity.Customer;
import com.example.meat_home.entity.Order;
import com.example.meat_home.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final OrderRepository orderRepository;

    public CustomerDto toDto(Customer customer) {
        if (customer == null) return null;

        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setPassword(customer.getPassword());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setOrders_id(customer.getOrders()
                .stream()
                .map(Order::getId)
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
                dto.getOrders_id()
                        .stream()
                        .map(id -> orderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Order not found: " + id)))
                        .collect(Collectors.toList())
        );

        return customer;
    }
}


//import org.mapstruct.Mapper;
//
//@Mapper(componentModel = "spring")
//public interface CustomerMapper {
//    CustomerDto toDto(Customer customer);
//    Customer toEntity(CustomerDto dto);
//}