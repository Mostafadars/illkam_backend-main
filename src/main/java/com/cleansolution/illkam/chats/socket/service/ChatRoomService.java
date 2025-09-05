package com.cleansolution.illkam.chats.socket.service;

import com.cleansolution.illkam.chats.dto.ChatsResponseDto;
import com.cleansolution.illkam.chats.dto.ChatsSaveRequestDto;
import com.cleansolution.illkam.chats.socket.controller.dto.CreateChatRoomResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatRoomService {
    CreateChatRoomResponse createChatRoomForPersonal(ChatsSaveRequestDto chatRoomRequest);

    ChatsResponseDto findById(String id);

    Boolean makeChatRoomActive(String id);

    ChatsResponseDto checkChatRoomExist(Long workid, Long applyid, Long employer);

//    ChatsResponseDto findByWorkAndRequest(Long workid, Long requestId, Long );


    List<ChatsResponseDto> findAllRequestChatByUserId(Long userid);

    List<ChatsResponseDto> findAllApplyChatByUserId(Long userid);

    Boolean exitChat( Long userid, String chatid);

    Page<ChatsResponseDto> getAllChats(int page);
}
