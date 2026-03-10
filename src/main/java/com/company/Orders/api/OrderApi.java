package com.company.Orders.api;

import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderApi {
    //firma del Api
    @PostMapping("/crear")
    ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO request);

    //cancelar orden solo con el ID
    @PostMapping("/cancelar/{id}")
    ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id);

}
