package com.company.Orders.apicontroller;

import com.company.Orders.api.OrderApi;
import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponseDTO;
import com.company.Orders.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderApiController implements OrderApi {
    //controlador para manejar las peticiones hacia el servicio
    private final OrderService orderService;
    @Override
    public ResponseEntity<OrderResponseDTO> createOrder(CreateOrderRequestDTO request) {
        // TODO 1: Simular el ID del usuario.
        // Como por seguridad el frontend no nos envía el userId en el JSON, y aún no
        // implementamos la lectura del token JWT, vamos a "quemar" un ID para poder probar.
        // Crea una variable de tipo Long llamada 'mockUserId' y asígnale el valor 1L.


        // TODO 2: Llamar a la lógica de negocio.
        // Usa el 'orderService' que ya está inyectado arriba y llama a su método 'createOrder'.
        // Pásale el 'request' (que trae el producto y cantidad) y tu 'mockUserId'.
        // Guarda lo que te devuelva en una variable de tipo OrderResponseDTO.


        // TODO 3: Construir la respuesta HTTP.
        // Retorna un ResponseEntity configurado con el status 201 (CREATED).
        // En el cuerpo (.body) de la respuesta, pon la variable que obtuviste en el paso 2.
        return null;
    }
}
