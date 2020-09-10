package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.IPRandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @PostMapping("/message/ip/{topicName}/{count}")
    public void send1(@RequestBody String message,@PathVariable(name = "count") long count,@PathVariable(name = "topicName") String topicName) throws InterruptedException {
        long start = System.currentTimeMillis();
        long temp = count;
        JSONObject json = JSON.parseObject(message);
        Random random = new Random();
        while (count > 0 ){
           json.put("sip",IPRandomUtils.getRandomIp());
            json.put("stime",System.currentTimeMillis());
            json.put("sport",random.nextInt(90000));
            kafkaTemplate.send(topicName,json.toString());
            count--;
            if (count % 1000 ==0){
                Thread.sleep(1000);
            }
        }
    }
    @PostMapping("/message/{topicName}")
    public boolean sendDomain(@RequestBody String message,@PathVariable(name = "topicName") String topicName){
        kafkaTemplate.send(topicName,message);
        return true;
    }

    @GetMapping("/test")
    public String test(@RequestParam String user_token){
       JSONObject json = new JSONObject();
       json.put("status","0000");
       json.put("result","0000");

        return json.toString();
    }
}