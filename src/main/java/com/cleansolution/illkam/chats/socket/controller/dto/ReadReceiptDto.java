package com.cleansolution.illkam.chats.socket.controller.dto;

import lombok.Getter;

@Getter
public class ReadReceiptDto {
    private String roomId;      // 방 ID
    private Long userId;      // 읽은 사용자 ID

    // Getters and Setters
}
