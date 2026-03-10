package com.company.Orders.publisher;

import com.company.Orders.events.OrderCancelledEvent;
import com.company.Orders.events.OrderCreatedEvent;

public interface OrderEventPublisher {

    //metodo que va ser llamado por el OrderServiceImpl
    void publishOrderCreated(OrderCreatedEvent event);

    //metodo para publicar evento de cancelacion
    void publishOrderCancelled(OrderCancelledEvent event);
}
