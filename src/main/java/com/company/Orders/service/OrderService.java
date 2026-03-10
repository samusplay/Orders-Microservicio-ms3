package com.company.Orders.service;

import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(CreateOrderRequestDTO request, Long userId);

    //cancelar orden por ID
    OrderResponseDTO cancelOrder(Long orderId);
}
