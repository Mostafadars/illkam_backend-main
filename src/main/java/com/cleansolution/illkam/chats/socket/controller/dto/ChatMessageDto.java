package com.cleansolution.illkam.chats.socket.controller.dto;

import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public  class ChatMessageDto {
    private String roomId;
    private String authorId;
    private String message;
    private String receiverId;

    /* Dto -> Entity */
    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .roomId(roomId)
                .authorId(authorId)
                .message(message)
                .receiverId(receiverId)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }
}