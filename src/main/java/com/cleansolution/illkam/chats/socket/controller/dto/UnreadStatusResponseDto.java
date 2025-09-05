package com.cleansolution.illkam.chats.socket.controller.dto;

import lombok.Getter;

@Getter
public class UnreadStatusResponseDto {
    private final String roomId;
    private final Integer unreadCount;

    public UnreadStatusResponseDto(String roomId, Integer unreadCount){
        this.roomId = roomId;
        this.unreadCount = unreadCount;
    }
}
