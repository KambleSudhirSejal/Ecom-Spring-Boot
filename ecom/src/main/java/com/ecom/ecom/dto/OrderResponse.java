package com.ecom.ecom.dto;

import com.ecom.ecom.models.OrderItemDTO;
import com.ecom.ecom.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private BigDecimal  totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
