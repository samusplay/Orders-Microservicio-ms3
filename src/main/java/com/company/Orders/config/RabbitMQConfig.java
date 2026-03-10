package com.company.Orders.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    // NOMBRES
    public static final String ORDER_EXCHANGE       = "order.exchange";
    public static final String ORDER_CREATED_QUEUE  = "order.created.queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created.v1";

    // Cancelacion
    public static final String ORDER_CANCELLED_QUEUE = "order.cancelled.queue";
    public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled.v1";

    // exchange
    @Bean
    // decide que cola va el mensaje
    public TopicExchange orderExchange(){
        return new TopicExchange(ORDER_EXCHANGE);
    }

    // Creacion Cola
    @Bean
    // guardar mensajes hasta que alguien los lea
    public Queue orderCreatedQueue(){
        return new Queue(ORDER_CREATED_QUEUE);
    }

    // Cola de cancelacion
    @Bean
    public Queue orderCancelledQueue(){
        return new Queue(ORDER_CANCELLED_QUEUE);
    }

    @Bean
    // configuramos el Routing key
    public Binding orderCreatedBinding(Queue orderCreatedQueue,TopicExchange orderExchange){
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(orderExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    // Binding de cancelacion
    @Bean
    public Binding orderCancelledBinding(Queue orderCancelledQueue, TopicExchange orderExchange){
        return BindingBuilder
                .bind(orderCancelledQueue)
                .to(orderExchange)
                .with(ORDER_CANCELLED_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // Aquí le decimos que use el conversor JSON que creaste antes
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    //Traducir a Json
    @Bean
    public MessageConverter jsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

}

