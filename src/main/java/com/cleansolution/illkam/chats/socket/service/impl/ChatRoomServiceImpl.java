package com.cleansolution.illkam.chats.socket.service.impl;

import com.cleansolution.illkam.chats.dto.ChatsResponseDto;
import com.cleansolution.illkam.chats.dto.ChatsSaveRequestDto;
import com.cleansolution.illkam.chats.socket.controller.dto.CreateChatRoomResponse;
import com.cleansolution.illkam.chats.socket.repository.ChatRoomRepository;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatRoom;
import com.cleansolution.illkam.chats.socket.service.ChatRoomService;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.WorksRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final UsersRepository userRepository;
    private final WorksRepository worksRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override // 개인 DM방 생성
    public CreateChatRoomResponse createChatRoomForPersonal(ChatsSaveRequestDto chatRoomRequest) {
        Users employee = userRepository.findById(chatRoomRequest.getEmployee_id()).orElseThrow(() -> new IllegalArgumentException("채팅방 생성하는 유저가 존재하지 않습니다."));
        Works work = worksRepository.findById(chatRoomRequest.getWork_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 일깜이 없습니다. id=" + chatRoomRequest.getWork_id()));
        Users employer = work.getEmployer();
        ChatRoom newRoom  = ChatRoom.create(employer, employee,work);
        newRoom.addMembers(employer, employee);
        chatRoomRepository.save(newRoom);
        return new CreateChatRoomResponse(employer.getId(),employee.getId(), newRoom.getId());
    }

    @Override
    public ChatsResponseDto findById(String id) {
        ChatRoom chatRoom= chatRoomRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("채팅 내역 없음"));
        return new ChatsResponseDto(chatRoom);
    }

    @Override
    @Transactional
    public Boolean makeChatRoomActive(String id) {
        ChatRoom chatRoom= chatRoomRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("채팅 내역 없음"));
        chatRoom.makeChatActive();
        return true;
    }

    @Override
    @Transactional
    public ChatsResponseDto checkChatRoomExist(Long workid, Long applyid, Long employerId) {
        ChatRoom entity = chatRoomRepository.findByWorkIdAndEmployerIdAndEmployeeId(workid,employerId,applyid)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다."));
        entity.setEmployeeExist(true);
        System.out.println(entity);
        return new ChatsResponseDto(entity);
    }

//    @Override
//    @Transactional
//    public ChatsResponseDto findByWorkAndRequest(Long workid, Long requestId) {
//        ChatRoom entity = chatRoomRepository.findByWorkIdAndEmployerIdAndEmployeeId(workid,requestId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다."));
//        entity.setEmployerExist(true);
//        return new ChatsResponseDto(entity);
//    }

    @Override
    public List<ChatsResponseDto> findAllRequestChatByUserId(Long id) {
        return chatRoomRepository.findAllByEmployerIdOrderByModifiedDateTimeDesc(id)
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatsResponseDto> findAllApplyChatByUserId(Long id) {
        return chatRoomRepository.findAllByEmployeeIdOrderByModifiedDateTimeDesc(id)
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean exitChat(Long userid, String chatid) {
        try {
            ChatRoom entity = chatRoomRepository.findById(chatid)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다. id=" + chatid));
            if (Objects.equals(entity.getEmployer().getId(), userid)) {
                entity.setEmployerExist(false);
            } else {
                entity.setEmployeeExist(false);
            }
            chatRoomRepository.save(entity);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Page<ChatsResponseDto> getAllChats(int page) {
        Sort sort = Sort.by("createdDateTime").descending();
        Pageable pageable = PageRequest.of(page, 10,sort);
        Page<ChatRoom> chatRoomPage = chatRoomRepository.findAll(pageable);
        List<ChatsResponseDto> chatRoomDTOs = chatRoomPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(chatRoomDTOs, pageable, chatRoomPage.getTotalElements());
    }


    private ChatsResponseDto convertToDTO(ChatRoom chats) {

        return new ChatsResponseDto(chats);
    }

}