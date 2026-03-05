package com.company.Orders.models;

import lombok.Data;

//dto que va validar el stock
@Data
public class StockCheckRequest {
    private Long productId;

    private Integer quantity;
}
