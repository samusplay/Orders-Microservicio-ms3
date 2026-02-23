package com.company.Orders.api;

import com.company.Orders.models.OrdersPruebaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrdersPruebaApi {

    @PostMapping("/crear")
    ResponseEntity<OrdersPruebaDTO>createTest(@RequestBody OrdersPruebaDTO dto);

    @GetMapping("/{id}")
    ResponseEntity<OrdersPruebaDTO>getTest(@PathVariable Long id);
}
