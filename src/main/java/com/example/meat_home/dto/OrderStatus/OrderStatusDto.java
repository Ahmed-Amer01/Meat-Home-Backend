package com.example.meat_home.dto.OrderStatus;

import com.example.meat_home.entity.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {
    @JsonIgnore
    private Long id;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
//    private Long order_id;
}
