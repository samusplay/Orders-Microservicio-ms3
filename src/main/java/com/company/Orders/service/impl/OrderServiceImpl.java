package com.company.Orders.service.impl;

import com.company.Orders.client.CatalogClient;
import com.company.Orders.entity.Order;
import com.company.Orders.events.OrderCancelledEvent;
import com.company.Orders.events.OrderCreatedEvent;
import com.company.Orders.exception.InsufficientStockException;
import com.company.Orders.models.CreateOrderRequestDTO;
import com.company.Orders.models.OrderResponseDTO;
import com.company.Orders.models.OrderStatus;
import com.company.Orders.models.StockCheckRequest;
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
    //Cliente Rest Catalogo
    private final CatalogClient catalogClient;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request, Long userId) {

        //validacion con Catalog
        //Id de rastreo
        String correlationId = UUID.randomUUID().toString();
        //creamos el objeto para catalogo
        StockCheckRequest stockRequest = new StockCheckRequest();
        stockRequest.setProductId(request.getProductId());
        stockRequest.setQuantity(request.getQuantity());

        //llamada HTTp
        Boolean hasStock = catalogClient.checkStock(stockRequest, correlationId);

        //si no hay stock devolver 409
        if (!hasStock) {
            throw new InsufficientStockException("No hay stock suficiente para el producto" + request.getProductId());
        }
        //guardamos en la base de datos
        Order newOrder = Order.builder()
                .userId(userId)
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .status(OrderStatus.PENDING)
                .build();

        //guardar
        Order savedOrder = orderRepository.save(newOrder);

        // Construir el evento con metodo privado
        OrderCreatedEvent event = buildOrderCreatedEvent(savedOrder, correlationId);

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

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId) {
        //buscar la orden en la base de datos
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + orderId));

        //validar que no este ya cancelada
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("La orden ya fue cancelada anteriormente");
        }

        //cambiar el estado a CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        //construir y publicar evento de cancelacion para restaurar stock
        OrderCancelledEvent event = new OrderCancelledEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setCorrelationId(UUID.randomUUID().toString());
        event.setOrderId(savedOrder.getId());
        event.setProductId(savedOrder.getProductId());
        event.setQuantity(savedOrder.getQuantity());
        event.setReason("Orden cancelada por el usuario");
        event.setCancelledAt(LocalDateTime.now());

        //publicar evento a RabbitMQ
        orderEventPublisher.publishOrderCancelled(event);

        //retornar respuesta
        return OrderResponseDTO.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .status(savedOrder.getStatus())
                .message("Orden cancelada exitosamente")
                .build();
    }

    //metodo privado para mandar la solictud
    //
    private OrderCreatedEvent buildOrderCreatedEvent(Order order, String correlationId) {
        OrderCreatedEvent event = new OrderCreatedEvent();

        // El EventId sí es un UUID nuevo porque cada evento es único
        event.setEventId(UUID.randomUUID().toString());

        // El CorrelationId usa el parámetro que le pasamos para mantener el rastro
        event.setCorrelationId(correlationId);

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

