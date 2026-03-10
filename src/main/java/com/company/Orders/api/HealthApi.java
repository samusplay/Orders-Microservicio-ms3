package com.company.Orders.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface HealthApi {

    @GetMapping("/health")
    ResponseEntity<String> healthCheck();
}
