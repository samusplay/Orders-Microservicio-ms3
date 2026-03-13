package com.company.Orders.service.impl;

import com.company.Orders.client.CatalogClient;
import com.company.Orders.entity.Order;
import com.company.Orders.entity.OrderItem;
import com.company.Orders.events.OrderCancelledEvent;
import com.company.Orders.events.OrderCreatedEvent;
import com.company.Orders.exception.InsufficientStockException;
import com.company.Orders.models.*;
import com.company.Orders.publisher.OrderEventPublisher;
import com.company.Orders.repository.OrderRepository;
import com.company.Orders.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request, Long userId, String correlationId) {
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        // 1. Validar stock en Catálogo para CADA producto
        for (ItemRequestDTO item : request.getItems()) {
            StockCheckRequest stockRequest = new StockCheckRequest();
            stockRequest.setProductId(item.getProductId());
            stockRequest.setQuantity(item.getQuantity());

            Boolean hasStock = catalogClient.checkStock(stockRequest, correlationId);
            if (!hasStock) {
                throw new InsufficientStockException("No hay stock suficiente para el producto ID: " + item.getProductId());
            }
        }

        // 2. Crear la Orden Maestra
        Order newOrder = Order.builder()
                .userId(userId)
                .status(OrderStatus.PENDING)
                .build();

        // 3. Agregar los detalles (OrderItems) a la Orden
        for (ItemRequestDTO itemRequest : request.getItems()) {
            OrderItem item = OrderItem.builder()
                    .productId(itemRequest.getProductId())
                    .quantity(itemRequest.getQuantity())
                    .build();
            newOrder.addItem(item); // Usa el método bidireccional que creamos en la entidad
        }

        // 4. Guardar en Base de Datos
        Order savedOrder = orderRepository.save(newOrder);

        // 5. Publicar eventos en RabbitMQ (Uno por cada producto)
        for (OrderItem item : savedOrder.getItems()) {
            OrderCreatedEvent event = buildOrderCreatedEvent(savedOrder, item, correlationId);
            orderEventPublisher.publishOrderCreated(event);
        }

        return OrderResponseDTO.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .status(savedOrder.getStatus())
                .message("Orden creada exitosamente")
                // Mapeamos los items de Entity a DTO para la respuesta
                .items(savedOrder.getItems().stream().map(item ->
                        new OrderItemResponseDTO(item.getId(), item.getProductId(), item.getQuantity())
                ).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId, Long userId,String correlationId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Acceso denegado: No tienes permiso para cancelar esta orden");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("La orden ya fue cancelada anteriormente");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        // Publicar eventos de cancelación (Uno por cada producto para que Catálogo reponga)
        for (OrderItem item : savedOrder.getItems()) {
            OrderCancelledEvent event = new OrderCancelledEvent();
            event.setEventId(UUID.randomUUID().toString());
            event.setCorrelationId(correlationId != null ? correlationId : UUID.randomUUID().toString());
            event.setOrderId(savedOrder.getId());

            // Asignamos datos del item individual
            event.setProductId(item.getProductId());
            event.setQuantity(item.getQuantity());

            event.setReason("Orden cancelada por el usuario");
            event.setCancelledAt(LocalDateTime.now());

            orderEventPublisher.publishOrderCancelled(event);
        }

        return OrderResponseDTO.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .status(savedOrder.getStatus())
                .message("Orden cancelada exitosamente")
                .items(savedOrder.getItems().stream().map(item ->
                        new OrderItemResponseDTO(item.getId(), item.getProductId(), item.getQuantity())
                ).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream().map(order ->
                OrderResponse.builder()
                        .id(order.getId())
                        .userId(order.getUserId())
                        .status(order.getStatus())
                        // Convertimos los Entity Items a DTO Items
                        .items(order.getItems().stream().map(item ->
                                OrderItemResponseDTO.builder()
                                        .id(item.getId())
                                        .productId(item.getProductId())
                                        .quantity(item.getQuantity())
                                        .build()
                        ).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
    }


    //metodo privado para mandar la solictud
    //
    private OrderCreatedEvent buildOrderCreatedEvent(Order order, OrderItem item, String correlationId) {
        OrderCreatedEvent event = new OrderCreatedEvent();

        event.setEventId(UUID.randomUUID().toString());
        event.setCorrelationId(correlationId);
        event.setOrderId(order.getId());
        event.setEstadoCompra(order.getStatus().name());
        event.setUserId(order.getUserId());

        // Ahora saca el ID y Cantidad del ITEM, no de la Orden maestra
        event.setProductId(item.getProductId());
        event.setQuantity(item.getQuantity());

        event.setTotalAmount(0.0);
        event.setCreatedAt(LocalDateTime.now());

        return event;
    }

}

