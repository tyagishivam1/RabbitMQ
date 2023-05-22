package com.rabbit.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderExchangeConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.header.queue-1}")
    private String QUEUE_NAME_1;

    @Value("${rabbitmq.header.queue-2}")
    private String QUEUE_NAME_2;

    @Value("${rabbitmq.header.exchange}")
    private String HEADER_EXCHANGE;

    Queue createHeaderQueue1(){
        return new Queue(QUEUE_NAME_1,true,false,false);
    }

    Queue createHeaderQueue2(){
        return new Queue(QUEUE_NAME_2,true,false,false);
    }

    HeadersExchange createHeaderExchange(){
        return new HeadersExchange(HEADER_EXCHANGE,true,false);
    }

    Binding createHeaderBinding1(){
        return BindingBuilder.bind(createHeaderQueue1()).to(createHeaderExchange())
                .whereAll("error","debug")
                .exist();
    }

    Binding createHeaderBinding2(){
        return BindingBuilder.bind(createHeaderQueue2())
                .to(createHeaderExchange())
                .whereAny("info","warning")
                .exist();
    }

    @Bean
    public AmqpTemplate headerExchange(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(HEADER_EXCHANGE);

        return  rabbitTemplate;
    }
    @PostConstruct
    public void init(){
        amqpAdmin.declareQueue(createHeaderQueue1());
        amqpAdmin.declareQueue(createHeaderQueue2());
        amqpAdmin.declareExchange(createHeaderExchange());
        amqpAdmin.declareBinding(createHeaderBinding1());
        amqpAdmin.declareBinding(createHeaderBinding2());
    }


}
