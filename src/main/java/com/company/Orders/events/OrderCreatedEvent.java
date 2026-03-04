package com.company.Orders.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

//clase que va viajar por rabbit
@Data
@NoArgsConstructor
@AllArgsConstructor
//Esta clase se va convertir en un JSON
public class OrderCreatedEvent implements Serializable {

    private String eventId;

    private String correlationId;

    private Long orderId;
    private String estadoCompra;

    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double totalAmount;
    //formatear la fecha
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
