package com.company.Orders.apicontroller;

import com.company.Orders.api.OrderApi;
import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponse;
import com.company.Orders.models.OrderResponseDTO;
import com.company.Orders.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class OrderApiController implements OrderApi {
    //controlador para manejar las peticiones hacia el servicio
    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderResponseDTO> createOrder(CreateOrderRequestDTO request, String userIdHeader, String correlationId) {
        // 1. Convertimos el String a Long
        Long realUserId = Long.valueOf(userIdHeader);

        System.out.println("Creando orden para el usuario ID: " + realUserId + " | CorrelationID: " + correlationId);

        // 2. Pasamos el ID real al Service y el correlationId
        OrderResponseDTO response = orderService.createOrder(request, realUserId,correlationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<OrderResponseDTO> cancelOrder(Long id, String userIdHeader, String correlationId) {
        Long realUserId = Long.valueOf(userIdHeader);

        System.out.println("Cancelando orden " + id + " para el usuario ID: " + realUserId + " | CorrelationID: " + correlationId);

        // Aquí pasamos el ID de la orden. (Ojo al siguiente paso)
        OrderResponseDTO response = orderService.cancelOrder(id, realUserId, correlationId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(Long userId) {
        //retornamos servicio
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}

