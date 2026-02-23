package com.company.Orders.apicontroller;

import com.company.Orders.api.OrdersPruebaApi;
import com.company.Orders.models.OrdersPruebaDTO;
import com.company.Orders.service.OrdersPruebaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/prueba")
public class OrdersPruebaApiController implements OrdersPruebaApi {
    private final OrdersPruebaService service;
    @Override
    public ResponseEntity<OrdersPruebaDTO> createTest(OrdersPruebaDTO dto) {
        OrdersPruebaDTO create=service.create(dto);
        return new ResponseEntity<>(create, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OrdersPruebaDTO> getTest(Long id) {
        OrdersPruebaDTO finded=service.findById(id);
        return ResponseEntity.ok(finded);
    }
}
