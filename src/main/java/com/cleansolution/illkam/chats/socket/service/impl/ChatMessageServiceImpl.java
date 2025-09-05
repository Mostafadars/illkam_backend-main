package com.cleansolution.illkam.chats.socket.service.impl;

import com.cleansolution.illkam.chats.socket.controller.dto.ChatMessageDto;
import com.cleansolution.illkam.chats.socket.controller.dto.UnreadStatusResponseDto;
import com.cleansolution.illkam.chats.socket.repository.ChatMessageRepository;
import com.cleansolution.illkam.chats.socket.repository.ChatRoomRepository;
import com.cleansolution.illkam.chats.socket.repository.UnreadChatMessageRepository;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatRoom;
import com.cleansolution.illkam.chats.socket.repository.entity.UnreadChatMessage;
import com.cleansolution.illkam.chats.socket.service.ChatMessageService;
import com.cleansolution.illkam.users.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UnreadChatMessageRepository unreadChatMessageRepository;

    @Override
    @Transactional
    public ChatMessage createChatMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = chatMessageDto.toEntity();
        System.out.println(chatMessage.getRoomId());
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getRoomId()).orElseThrow();
        chatRoom.setLastChatMesg(chatMessage);
        chatRoom.makeChatActive();
        chatRoomRepository.save(chatRoom);

        Users receiver = Objects.equals(chatMessageDto.getAuthorId(), chatRoom.getEmployee().getId().toString()) ? chatRoom.getEmployer() : chatRoom.getEmployee();

        if (!receiver.getId().toString().equals(chatMessageDto.getAuthorId())) {
            UnreadChatMessage unreadMessage = unreadChatMessageRepository
                    .findByReaderIdAndChatRoomId(receiver.getId(), chatMessage.getRoomId())
                    .orElse(new UnreadChatMessage(receiver, chatRoomRepository.findById(chatMessage.getRoomId()).orElseThrow()));
            unreadMessage.setUnreadCount(unreadMessage.getUnreadCount() + 1);
            unreadChatMessageRepository.save(unreadMessage);
        }

        return chatMessage;
    }

    @Override
    public Page<ChatMessage> getChatHistory(String chatId, Pageable pageable) {

        Page<ChatMessage> messages = chatMessageRepository.findAllByRoomId(chatId, pageable);

// Adjust createdAt to local time by adding 9 hours
//        messages.forEach(message -> {
//            message.setCreatedAt(message.getCreatedAt().plusHours(9));
//        });

        return messages;
    }

    @Override
    @Transactional
    public Void readChatByUser(String chatId, Long userid) {
        chatMessageRepository.updateReadCountForMessages(chatId, userid);
        Optional<UnreadChatMessage> unreadChatMessage= unreadChatMessageRepository.findByReaderIdAndChatRoomId(userid, chatId);
        unreadChatMessage.ifPresent(chatMessage -> chatMessage.setUnreadCount(0));
        return null;
    }

    @Override
    @Transactional
    public Void receivedDuringEntered(Long messageId) {
        chatMessageRepository.updateReadCountByMessageId(messageId);

        return null;
    }

    @Override
    public List<UnreadStatusResponseDto> getUnreadMessages(Long userid) {
        List<UnreadChatMessage> unreadMessages = unreadChatMessageRepository.findByReaderId(userid);
        return unreadMessages.stream()
                .map((elem) -> new UnreadStatusResponseDto(elem.getChatRoom().getId(), elem.getUnreadCount())).toList();
    }


}