package com.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener {

    @KafkaListener(topics = "skyeye-tcpflow_demo_no")
    public void onMessage(String message){

        System.out.println(message);
    }

}