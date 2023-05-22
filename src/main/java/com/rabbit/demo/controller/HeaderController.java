package com.rabbit.demo.controller;

import com.rabbit.demo.model.QueueObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HeaderController {
    @Autowired
    private AmqpTemplate headerExchange;

    @PostMapping("/header")
    public ResponseEntity<?> sendMessageWithHeaderExchange(
    @RequestParam(value = "error",required = false) String error,
    @RequestParam(value = "debug",required = false) String debug,
    @RequestParam(value = "info",required = false) String info,
    @RequestParam(value = "warning",required = false) String warning
    )
    {
        QueueObject object=new QueueObject("header", LocalDateTime.now());
        MessageBuilder messageBuilder=MessageBuilder.withBody(object.toString().getBytes());

        if(error != null){
            messageBuilder.setHeader("error",error);
        }
        if (debug != null){
            messageBuilder.setHeader("debug",debug);
        }
        if (info != null){
            messageBuilder.setHeader("info",info);
        }
        if (warning != null){
            messageBuilder.setHeader("warning",warning);
        }
        headerExchange.convertAndSend(messageBuilder.build());
        return ResponseEntity.ok(true);
    }
}
