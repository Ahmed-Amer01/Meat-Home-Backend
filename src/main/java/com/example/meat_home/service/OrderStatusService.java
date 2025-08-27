package com.example.meat_home.service;

import com.example.meat_home.dto.OrderStatus.OrderStatusDto;
import com.example.meat_home.repository.OrderRepository;
import com.example.meat_home.repository.OrderStatusChangeRepository;
import com.example.meat_home.util.OrderStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final OrderStatusChangeRepository osRepository;
    private final OrderStatusMapper osMapper;

    public List<OrderStatusDto> findByOrderId(Long orderId) {
        return osRepository.findByOrderId(orderId).stream()
                .map(osMapper::toDto)
                .toList();
    }
}
