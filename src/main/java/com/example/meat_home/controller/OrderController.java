package com.example.meat_home.controller;

import com.example.meat_home.dto.Order.CreateOrderDto;
import com.example.meat_home.dto.Order.OrderDto;
import com.example.meat_home.dto.Order.UpdateOrderDto;
import com.example.meat_home.dto.OrderStatus.OrderStatusDto;
import com.example.meat_home.service.OrderService;
import com.example.meat_home.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        OrderDto order = orderService.getOrderById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }


    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderDto dto, UriComponentsBuilder uriBuilder) {
        OrderDto created = orderService.createOrder(dto);
        URI uri = uriBuilder.path("/api/orders/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderDto dto) {
        OrderDto updated = orderService.updateOrderPatch(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }


    // ✅ New endpoint: Get order statuses (the jira sub-task: T4-26 endpoint for any user to get the status history of an order)
    @GetMapping("/{id}/statuses")
    public ResponseEntity<List<OrderStatusDto>> getOrderStatuses(@PathVariable Long id) {
        List<OrderStatusDto> statuses = orderStatusService.findByOrderId(id);
        return ResponseEntity.ok(statuses);
    }
}
