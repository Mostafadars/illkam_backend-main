package com.cleansolution.illkam.chats.socket;

import com.cleansolution.illkam.chats.socket.controller.dto.ReadReceiptDto;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component

public class RedisMessageSubscriber implements MessageListener {


    private final SimpMessagingTemplate messagingTemplate;

    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Redis로부터 메시지를 수신한 경우 WebSocket 클라이언트에 전송
        System.out.println("redis message 수신");
        String channel = new String(pattern);
        System.out.println(channel);
        String body = new String(message.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        if(channel.equals("chatroom")){
            try {
                // JSON 문자열을 ChatMessage 객체로 변환
                ChatMessage chatMessage = objectMapper.readValue(body, ChatMessage.class);
                messagingTemplate.convertAndSend("/sub/channel/"+chatMessage.getRoomId(), body);
                messagingTemplate.convertAndSend("/sub/unread/"+chatMessage.getReceiverId() , body);
            } catch (JsonProcessingException e) {
                System.err.println("Failed to parse message: " + body);
                e.printStackTrace();
            }
        }
        if(channel.equals("read")){
            try {
                // JSON 문자열을 ChatMessage 객체로 변환
                ReadReceiptDto readReceipt = objectMapper.readValue(body, ReadReceiptDto.class);
                messagingTemplate.convertAndSend("/sub/channel/"+readReceipt.getRoomId()+"/read", readReceipt);
            } catch (JsonProcessingException e) {
                System.err.println("Failed to parse message: " + body);
                e.printStackTrace();
            }
        }



    }


//    ㅅㄷㄴㅅ


}
