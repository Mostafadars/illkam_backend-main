package com.cleansolution.illkam.chats;

import com.cleansolution.illkam.chats.dto.ChatsResponseDto;
import com.cleansolution.illkam.chats.dto.ChatsSaveRequestDto;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.WorksRepository;
import com.cleansolution.illkam.works.dto.WorksResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatsService {
    private final ChatsRepository chatsRepository;
    private final UsersRepository usersRepository;
    private final WorksRepository worksRepository;

    public Long save(ChatsSaveRequestDto requestDto) {
        Users employee = usersRepository.findById(requestDto.getEmployee_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + requestDto.getEmployee_id()));
        Works work = worksRepository.findById(requestDto.getWork_id())
                .orElseThrow(() -> new IllegalArgumentException("해당 일깜이 없습니다. id=" + requestDto.getWork_id()));

        return chatsRepository.save(requestDto.toEntity(work.getEmployer(), employee, work)).getId();
    }

    public ChatsResponseDto getChatInfo(Long chatId) {

        Chats entity = chatsRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다. id=" + chatId));
//        return new ChatsResponseDto(entity);
        return null;
    }

    public Boolean makeChatRoomActive(Long chatId){
        Chats entity = chatsRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다. id=" + chatId));
        entity.makeChatActive();
        chatsRepository.save(entity);
        return true;
    }

    public ChatsResponseDto findChatByWorkAndApply(Long workId, Long applierId) {
        Chats entity = chatsRepository.findByWorkIdAndEmployeeId(workId,applierId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다."));
        entity.setEmployeeExist(true);
        chatsRepository.save(entity);
//        return new ChatsResponseDto(entity);
        return null;
    }

    public ChatsResponseDto findChatByWorkAndRequest(Long workId, Long requestId) {
        Chats entity = chatsRepository.findByWorkIdAndEmployerId(workId,requestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다."));
        entity.setEmployerExist(true);
        chatsRepository.save(entity);
//        return new ChatsResponseDto(entity);
        return null;
    }

    public List<ChatsResponseDto> findAllRequestChatByUserId(Long userid) {
        return chatsRepository.findAllByEmployerId(userid)
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ChatsResponseDto> findAllApplyChatByUserId(Long userid) {
        return chatsRepository.findAllByEmployeeId(userid)
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Boolean exitChat(Long chatId, Long userid) {
        try {
            Chats entity = chatsRepository.findById(chatId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 채팅이 없습니다. id=" + chatId));
            if (Objects.equals(entity.getEmployer().getId(), userid)) {
                entity.setEmployerExist(false);
            } else {
                entity.setEmployeeExist(false);
            }
            chatsRepository.save(entity);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private ChatsResponseDto convertToDTO(Chats chats) {
//        return new ChatsResponseDto(chats);
        return null;
    }

}
