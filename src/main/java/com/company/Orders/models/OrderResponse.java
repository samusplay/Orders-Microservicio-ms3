package com.company.Orders.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//dto para listar las ordenes
public class OrderResponse {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private String message; // (Si lo tienes en OrderResponseDTO)

    // Quitamos productId y quantity y ponemos la lista
    private List<OrderItemResponseDTO> items;
}
