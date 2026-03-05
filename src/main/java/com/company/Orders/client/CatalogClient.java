package com.company.Orders.client;

import com.company.Orders.models.StockCheckRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

//comunicacion puerto interno
@FeignClient(name="catalog-service",url="http://localhost:8082")
public interface CatalogClient {

    //endpoint que apunta de Catalog
    @PostMapping("/check-stock")
    Boolean checkStock(
            @RequestBody StockCheckRequest request,
            //propaga el Correlation-id
            @RequestHeader("X-Correlation-Id") String correlationId
            );
}
