package com.company.Orders.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent implements Serializable {

    private String eventId;
    private String correlationId;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private String reason;

    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime cancelledAt;
}
