package com.example.meat_home.dto.Order;

import com.example.meat_home.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDto {
    /**
     * Map of product IDs to quantities.
     * The key is the product ID and the value is the quantity.
     */
    private Map<Long, Integer> products;
    
    /**
     * Optional status update for the order.
     * If provided, will add a new status change to the order.
     */
    private StatusEnum status;
}
