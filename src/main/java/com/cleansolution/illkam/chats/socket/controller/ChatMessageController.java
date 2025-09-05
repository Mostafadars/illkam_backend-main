package com.cleansolution.illkam.chats.socket.controller;

import com.cleansolution.illkam.chats.socket.RedisPublisher;
import com.cleansolution.illkam.chats.socket.controller.dto.ChatMessageDto;
import com.cleansolution.illkam.chats.socket.controller.dto.ReadReceiptDto;
import com.cleansolution.illkam.chats.socket.repository.ChatMessageRepository;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import com.cleansolution.illkam.chats.socket.service.ChatMessageService;
import com.cleansolution.illkam.firebase.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisPublisher redisPublisher;
    private final FCMService fcmService;

    @MessageMapping("/message")
    public void sendMessage(ChatMessageDto message) {
        // 실시간으로 방에서 채팅하기
        ChatMessage newChat = chatMessageService.createChatMessage(message);
        try{
            fcmService.sendChatFcm(newChat);
        }catch(Exception error){
            System.out.println(error);
        }
        log.info("received message: {}", message);
        // 레디스 사용 햇을 때
//        redisPublisher.publish("chatroom", newChat);
        // 로컬만 사용
        messagingTemplate.convertAndSend("/sub/channel/"+message.getRoomId(), newChat);

        messagingTemplate.convertAndSend("/sub/unread/"+message.getReceiverId() , newChat);
    }

    @MessageMapping("/message/read") // 클라이언트에서 읽음 상태 전송
    public Void handleReadReceipt(ReadReceiptDto readReceipt) {
        System.out.println("호출됨");
        System.out.println(readReceipt.getUserId());
        // 1. 읽음 상태를 데이터베이스에 업데이트
        chatMessageRepository.updateReadCountByChatId(readReceipt.getRoomId(), readReceipt.getUserId());
        log.info("message read: {}", readReceipt);
        // 방에 있는 모든 사용자에게 메시지 전송
//        redisPublisher.publishRead("read", readReceipt);
        messagingTemplate.convertAndSend("/sub/channel/"+readReceipt.getRoomId()+"/read", readReceipt);
//        // 2. 읽음 상태를 다른 사용자들에게 브로드캐스트
//        return new ReadReceipt(readReceipt.getRoomId(), readReceipt.getMessageId(), readReceipt.getUserId());
        return null;
    }


}