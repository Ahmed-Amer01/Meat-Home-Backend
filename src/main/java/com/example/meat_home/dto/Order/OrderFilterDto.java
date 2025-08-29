package com.example.meat_home.dto.Order;

import com.example.meat_home.entity.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFilterDto {
    private Long customerId;
    private StatusEnum status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
