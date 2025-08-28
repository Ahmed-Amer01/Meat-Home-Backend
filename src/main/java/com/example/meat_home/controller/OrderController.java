package com.example.meat_home.controller;

import com.example.meat_home.dto.Order.CreateOrderDto;
import com.example.meat_home.dto.Order.OrderDto;
import com.example.meat_home.dto.Order.UpdateOrderDto;
import com.example.meat_home.dto.OrderStatus.OrderStatusDto;
import com.example.meat_home.entity.StatusEnum;
import com.example.meat_home.service.OrderService;
import com.example.meat_home.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    @GetMapping
    @PreAuthorize("hasRole('CALLCENTER') or hasRole('ADMIN') or hasRole('DELIVERY')")
    public ResponseEntity<List<OrderDto>> getOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) StatusEnum status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        
        if (customerId != null || status != null || startDate != null || endDate != null) {
            return ResponseEntity.ok(orderService.getFilteredOrders(customerId, status, startDate, endDate));
        }
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
    
    @PostMapping("/{id}/status/ready")
    @PreAuthorize("hasRole('CALLCENTER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> markOrderAsReady(@PathVariable Long id) {
        OrderDto updatedOrder = orderService.updateOrderStatus(id, StatusEnum.Ready);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @GetMapping("/status/ready")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DELIVERY')")
    public ResponseEntity<List<OrderDto>> getReadyOrders() {
        List<OrderDto> readyOrders = orderService.getOrdersByStatus(StatusEnum.Ready);
        return ResponseEntity.ok(readyOrders);
    }

    @PostMapping("/{id}/status/on-way")
    @PreAuthorize("hasRole('DELIVERY') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> markOrderAsOnWay(@PathVariable Long id) {
        OrderDto updatedOrder = orderService.updateOrderStatus(id, StatusEnum.OnWay);
        return updatedOrder != null ? 
            ResponseEntity.ok(updatedOrder) : 
            ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/status/delivered")
    @PreAuthorize("hasRole('DELIVERY') or hasRole('ADMIN')")
    public ResponseEntity<OrderDto> markOrderAsDelivered(@PathVariable Long id) {
        OrderDto updatedOrder = orderService.updateOrderStatus(id, StatusEnum.Delivered);
        return updatedOrder != null ? 
            ResponseEntity.ok(updatedOrder) : 
            ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long id) {
        OrderDto cancelledOrder = orderService.cancelOrder(id);
        return cancelledOrder != null ?
            ResponseEntity.ok(cancelledOrder) :
            ResponseEntity.notFound().build();
    }
}
