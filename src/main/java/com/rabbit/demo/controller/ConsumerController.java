package com.rabbit.demo.controller;

import com.rabbit.demo.Consumer;
import com.rabbit.demo.model.QueueObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
public class ConsumerController {

    @Autowired
    private Consumer consumer;

    @PostMapping("/consume/{queueName}")
    public ResponseEntity<?> sendMessageWithTopicExchange(@PathVariable String queueName){
       return ResponseEntity.ok(consumer.receiveMessage(queueName));
    }


}
