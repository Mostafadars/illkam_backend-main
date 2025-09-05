package com.cleansolution.illkam.chats.socket.controller.dto;


import lombok.Getter;

@Getter
public class CreateChatRoomRequest {
    private Long roomMakerId;
    private Long guestId;
}