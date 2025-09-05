package com.cleansolution.illkam.chats.socket;

import com.cleansolution.illkam.chats.socket.controller.dto.ReadReceiptDto;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String topic, ChatMessage message) {
        redisTemplate.convertAndSend(topic, message);
    }

    public void publishRead(String topic, ReadReceiptDto readReceipt) {
        redisTemplate.convertAndSend(topic, readReceipt);
    }
}
