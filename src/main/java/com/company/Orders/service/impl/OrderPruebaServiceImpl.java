package com.company.Orders.service.impl;

import com.company.Orders.entity.OrdersPrueba;
import com.company.Orders.events.OrderCreatedEvent;
import com.company.Orders.models.OrdersPruebaDTO;
import com.company.Orders.publisher.OrderEventPublisher;
import com.company.Orders.repository.OrdersPruebaRepository;
import com.company.Orders.service.OrdersPruebaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderPruebaServiceImpl implements OrdersPruebaService {
    private final OrdersPruebaRepository repository;
    private  final OrderEventPublisher orderEventPublisher;
    @Override
    public OrdersPruebaDTO create(OrdersPruebaDTO dto) {
        OrdersPrueba entity = new OrdersPrueba();
        entity.setEstadoCompra(dto.getEstadoCompra());

        OrdersPrueba guardado = repository.save(entity);

        //prueba Rabbit creamos evento
        OrderCreatedEvent event=new OrderCreatedEvent();
        event.setOrderId(guardado.getId());
        event.setEstadoCompra(guardado.getEstadoCompra());
        event.setCreatedAt(LocalDateTime.now());

        //publicamos el evento
        orderEventPublisher.publishOrderCreated(event);

        //debugeo
        System.out.println("✅ Orden guardada (ID " + guardado.getId() + ") y evento publicado en RabbitMQ");

        dto.setId(guardado.getId());
        return dto;
    }

    @Override
    public OrdersPruebaDTO findById(Long id) {
        OrdersPrueba entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la orden con ID: " + id));

        OrdersPruebaDTO dto = new OrdersPruebaDTO();
        dto.setId(entity.getId());
        dto.setEstadoCompra(entity.getEstadoCompra());
        return dto;
    }
}
