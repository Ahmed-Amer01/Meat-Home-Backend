package com.example.meat_home.controller;

import com.example.meat_home.dto.Customer.CustomerDto;
import com.example.meat_home.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor

public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        List<CustomerDto> customers = customerService.getCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomer(id);
        return customer != null ? ResponseEntity.ok(customer)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto orderDto, UriComponentsBuilder uriBuilder) {
        CustomerDto created = customerService.createCustomer(orderDto);
        URI uri = uriBuilder.path("/api/customers/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomerById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        CustomerDto updated = customerService.updateCustomerPatch(id, customerDto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.badRequest().build();
    }
}