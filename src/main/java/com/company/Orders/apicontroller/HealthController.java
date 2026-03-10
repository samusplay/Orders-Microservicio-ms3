package com.company.Orders.apicontroller;

import com.company.Orders.api.HealthApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements HealthApi {

    @Override
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Servicio de Órdenes funcionando correctamente");
    }
}
