package com.company.Orders.publisher.impl;

import com.company.Orders.config.RabbitMQConfig;
import com.company.Orders.events.OrderCreatedEvent;
import com.company.Orders.publisher.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisherImpl implements OrderEventPublisher {
    //inyectamos plantilla de rabbit
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        //envio a RabbitMq
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                event

        );
        //debugging consola
        System.out.println("Evento enviado a RabbitMQ"+event.getEventId());

    }
}
