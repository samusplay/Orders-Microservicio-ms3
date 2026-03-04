package com.company.Orders.service.impl;

import com.company.Orders.entity.Order;
import com.company.Orders.events.OrderCreatedEvent;
import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponseDTO;
import com.company.Orders.models.OrderStatus;
import com.company.Orders.publisher.OrderEventPublisher;
import com.company.Orders.repository.OrderRepository;
import com.company.Orders.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    //inyeccion dependencias
    private final OrderRepository orderRepository;
    //eventos rabbit
   private final OrderEventPublisher orderEventPublisher;
    @Override
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request, Long userId) {
        //guardamos en la base de datos
        Order newOrder=Order.builder()
                .userId(userId)
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status(OrderStatus.PENDING)
                .build();

        //guardar
        Order savedOrder=orderRepository.save(newOrder);

        // Construir el evento con metodo privado
        OrderCreatedEvent event = buildOrderCreatedEvent(savedOrder);

        //  Publicar el evento
        orderEventPublisher.publishOrderCreated(event);

        //retornamos respuesta
        return OrderResponseDTO.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .status(savedOrder.getStatus())
                .message("Orden creada exitosamente")
                .build();
    }

    //metodo privado para mandar la solictud
    private OrderCreatedEvent buildOrderCreatedEvent(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setCorrelationId(UUID.randomUUID().toString());
        event.setOrderId(order.getId());
        event.setEstadoCompra(order.getStatus().name());
        event.setUserId(order.getUserId());
        event.setProductId(order.getProductId());
        event.setQuantity(order.getQuantity());
        event.setTotalAmount(0.0); // luego calcular precio
        event.setCreatedAt(LocalDateTime.now());

        return event;
    }
}
