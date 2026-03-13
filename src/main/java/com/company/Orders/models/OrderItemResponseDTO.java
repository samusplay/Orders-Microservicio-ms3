package com.company.Orders.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.EmbeddedTable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {

    private Long id; // El ID del detalle en la tabla order_items
    private Long productId;
    private Integer quantity;
}
