package com.cleansolution.illkam.chats.socket.controller.dto;

import lombok.Getter;

@Getter
public class CreateChatRoomResponse {
    private Long employerId;
    private Long employeeId;
    private String chatRoomId;

    /* Entity -> Dto */
    public CreateChatRoomResponse(Long employerId, Long employeeId, String chatRoomId) {
        this.employerId = employerId;
        this.employeeId = employeeId;
        this.chatRoomId = chatRoomId;
    }
}