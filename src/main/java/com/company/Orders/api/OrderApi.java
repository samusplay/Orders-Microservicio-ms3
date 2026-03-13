package com.company.Orders.api;

import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponse;
import com.company.Orders.models.OrderResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface OrderApi {
    //firma del Api
    @PostMapping("/crear")
    ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody CreateOrderRequestDTO request,
            //trazabilidad saber que usuario hace
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId
    );

    //cancelar orden solo con el ID
    @PostMapping("/cancelar/{id}")
    ResponseEntity<OrderResponseDTO> cancelOrder(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId
    );

    @GetMapping("/user/{userId}")
    ResponseEntity<List<OrderResponse>> getOrdersByUserId(
            @PathVariable("userId") Long userId);


}
