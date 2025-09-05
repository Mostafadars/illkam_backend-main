package com.cleansolution.illkam.chats.socket.controller;

import com.cleansolution.illkam.chats.dto.ChatsResponseDto;
import com.cleansolution.illkam.chats.dto.ChatsSaveRequestDto;
import com.cleansolution.illkam.chats.socket.controller.dto.CreateChatRoomResponse;
import com.cleansolution.illkam.chats.socket.controller.dto.UnreadStatusResponseDto;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import com.cleansolution.illkam.chats.socket.service.ChatMessageService;
import com.cleansolution.illkam.chats.socket.service.ChatRoomService;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.WorksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final WorksRepository worksRepository;

    @PostMapping("/personal") //개인 DM 채팅방 생성
    public CreateChatRoomResponse createPersonalChatRoom(@RequestBody ChatsSaveRequestDto request) {
        return chatRoomService.createChatRoomForPersonal(request);
    }

    @GetMapping("/{id}")
    public ChatsResponseDto findById(@PathVariable("id") String id) {
        return chatRoomService.findById(id);
    }

    @GetMapping("/{id}/active")
    public Boolean makeChatRoomActive(@PathVariable("id") String id) {
        return chatRoomService.makeChatRoomActive(id);
    }

    @GetMapping("/{workid}/work/{applyid}/apply")
    public ChatsResponseDto findByWorkAndApply(@PathVariable("workid") Long workid, @PathVariable("applyid") Long applyid) {
        Works work = worksRepository.findById(workid).orElseThrow(()-> new IllegalArgumentException("asdf"));
        return chatRoomService.checkChatRoomExist(workid,applyid,work.getEmployer().getId());
    }

//    @GetMapping("/{workid}/work/{requestId}/request")
//    public ChatsResponseDto findByWorkAndRequest(@PathVariable("workid") Long workid, @PathVariable("requestId") Long requestId) {
//
//
//        return chatRoomService.checkChatRoomExist(workid,requestId);
//    }

    @GetMapping("/{userid}/request")
    public List<ChatsResponseDto> findAllRequestChatByUserId(@PathVariable("userid") Long id) {
        return chatRoomService.findAllRequestChatByUserId(id);
    }

    @GetMapping("/{userid}/apply")
    public List<ChatsResponseDto> findAllApplyChatByUserId(@PathVariable("userid") Long id) {
        return chatRoomService.findAllApplyChatByUserId(id);
    }

    @GetMapping("/{chatid}/exit/{userid}")
    public Boolean exitChat(@PathVariable("userid") Long userid,
                            @PathVariable("chatid") String chatid) {
        return chatRoomService.exitChat(userid,chatid);
    }

    @PostMapping("/")
    public CreateChatRoomResponse save(@RequestBody ChatsSaveRequestDto requestDto) {
        return chatRoomService.createChatRoomForPersonal(requestDto);
    }

    @GetMapping("/messages")
    public Page<ChatMessage> getMessageHistory(
            @RequestParam String chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size){
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<ChatMessage> messages = chatMessageService.getChatHistory(chatRoomId, pageable);
        return messages;
    }

    @GetMapping("/messages/{chatId}/read/{userid}")
    public Boolean enterChatRoom(@PathVariable("chatId") String chatId, @PathVariable("userid") Long userid){
        chatMessageService.readChatByUser(chatId, userid);
        return true;
    }

    @GetMapping("/messages/{messageId}/read")
    public Boolean receiveDuringEntered(@PathVariable("messageId") Long messageId){
        chatMessageService.receivedDuringEntered(messageId);
        return true;
    }

    @GetMapping("/unread/{userId}")
    public List<UnreadStatusResponseDto> getUnreadMessages(@PathVariable Long userId) {
        return chatMessageService.getUnreadMessages(userId);
    }

    @GetMapping()
    @CrossOrigin(origins = "http://localhost:3000") // 허용할 Origin 지정
    public Page<ChatsResponseDto> getAllChats(@RequestParam int page){
        return chatRoomService.getAllChats(page);
    }

}