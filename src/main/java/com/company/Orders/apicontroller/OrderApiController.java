package com.company.Orders.apicontroller;

import com.company.Orders.api.OrderApi;
import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponseDTO;
import com.company.Orders.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderApiController implements OrderApi {
    //controlador para manejar las peticiones hacia el servicio
    private final OrderService orderService;
    @Override
    public ResponseEntity<OrderResponseDTO> createOrder(CreateOrderRequestDTO request) {

        //auth prueba
        Long mockUserId = 1L;

        //llamada al service
        OrderResponseDTO response=orderService.createOrder(request,mockUserId);


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
