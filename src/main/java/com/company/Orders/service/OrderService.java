package com.company.Orders.service;

import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponse;
import com.company.Orders.models.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(CreateOrderRequestDTO request, Long userId, String correlationId);

    //cancelar orden por ID
    OrderResponseDTO cancelOrder(Long orderId,Long userId,String correlationId);

    //listar ordenes del usuario
    List<OrderResponse>getOrdersByUserId(Long userId);
}
