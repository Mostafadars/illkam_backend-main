package com.cleansolution.illkam.chats.socket.service;

import com.cleansolution.illkam.chats.socket.controller.dto.ChatMessageDto;
import com.cleansolution.illkam.chats.socket.controller.dto.UnreadStatusResponseDto;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageService {
    ChatMessage createChatMessage(ChatMessageDto chatMessageDto);

    Page<ChatMessage> getChatHistory(String chatId, Pageable pageable);

    Void readChatByUser(String chatId, Long userid);

    Void receivedDuringEntered(Long messageId);

    List<UnreadStatusResponseDto> getUnreadMessages(Long userid);


}
