package com.company.Orders.publisher;

import com.company.Orders.events.OrderCreatedEvent;

public interface OrderEventPublisher {

    //metodo que va ser llamado por el OrderServiceImpl
    void publishOrderCreated(OrderCreatedEvent event);
}
