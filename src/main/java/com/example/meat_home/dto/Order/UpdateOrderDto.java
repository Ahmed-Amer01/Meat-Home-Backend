package com.example.meat_home.dto.Order;

import com.example.meat_home.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDto {
    private List<Long> products_id;
    private StatusEnum status;
}
