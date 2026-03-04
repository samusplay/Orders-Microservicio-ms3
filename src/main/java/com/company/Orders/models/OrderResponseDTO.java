package com.company.Orders.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private OrderStatus status;
    private String message;
}
