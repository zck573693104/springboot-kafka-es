package com.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;


public class MyKafkaListener {
    @Autowired
    private Websocket websocket;

    /**
     * 发送聊天消息时的监听
     * @param record
     */
    @KafkaListener(topics = {"sendMessage"})
    public void listen(ConsumerRecord<String, String> record) {
        websocket.kafkaReceiveMsg(record.value());
    }

    /**
     * 关闭连接时的监听
     * @param record
     */
    @KafkaListener(topics = {"closeWebsocket"})
    private void closeListener(ConsumerRecord<String, String> record) {
        websocket.kafkaCloseWebsocket(record.value().toString());
    }

}