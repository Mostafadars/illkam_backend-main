package com.cleansolution.illkam.chats;


import com.cleansolution.illkam.chats.dto.ChatsResponseDto;
import com.cleansolution.illkam.chats.dto.ChatsSaveRequestDto;
import com.cleansolution.illkam.works.dto.WorksResponseDto;
import com.cleansolution.illkam.works.dto.WorksSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/chats")
@RequiredArgsConstructor
public class ChatsController {
    private final ChatsService chatsService;

    @GetMapping("/{id}")
    public ChatsResponseDto findById(@PathVariable("id") Long id) {
        return chatsService.getChatInfo(id);
    }

    @GetMapping("/{id}/active")
    public Boolean makeChatRoomActive(@PathVariable("id") Long id) {
        return chatsService.makeChatRoomActive(id);
    }

    @GetMapping("/{workid}/work/{applyid}/apply")
    public ChatsResponseDto findByWorkAndApply(@PathVariable("workid") Long workid, @PathVariable("applyid") Long applyid) {
        return chatsService.findChatByWorkAndApply(workid,applyid);
    }

    @GetMapping("/{workid}/work/{requestId}/request")
    public ChatsResponseDto findByWorkAndRequest(@PathVariable("workid") Long workid, @PathVariable("requestId") Long requestId) {
        return chatsService.findChatByWorkAndRequest(workid,requestId);
    }

    @GetMapping("/{userid}/request")
    public List<ChatsResponseDto> findAllRequestChatByUserId(@PathVariable("userid") Long id) {
        return chatsService.findAllRequestChatByUserId(id);
    }

    @GetMapping("/{userid}/apply")
    public List<ChatsResponseDto> findAllApplyChatByUserId(@PathVariable("userid") Long id) {
        return chatsService.findAllApplyChatByUserId(id);
    }

    @GetMapping("/{chatid}/exit/{userid}")
    public Boolean exitChat(@PathVariable("userid") Long userid,
                                            @PathVariable("chatid") Long chatid) {
        return chatsService.exitChat(chatid,userid);
    }

    @PostMapping("/")
    public Long save(@RequestBody ChatsSaveRequestDto requestDto) {
        return chatsService.save(requestDto);
    }

//    TODO void delete

}
