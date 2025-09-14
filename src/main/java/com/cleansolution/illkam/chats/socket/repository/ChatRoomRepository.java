package com.cleansolution.illkam.chats.socket.repository;

import com.cleansolution.illkam.chats.Chats;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {


//    List<ChatRoom> findAllByEmployeeId(Long employee);
//    List<ChatRoom>
    List<ChatRoom> findAllByEmployeeIdOrderByModifiedDateTimeDesc(Long employee);
    List<ChatRoom> findAllByEmployerIdOrderByModifiedDateTimeDesc(Long employer);
    Optional<ChatRoom> findByWorkIdAndEmployeeId(Long workId, Long employeeId);
    Optional<ChatRoom> findByWorkIdAndEmployerIdAndEmployeeId(Long workId, Long employerId, Long employeeId);

    List<ChatRoom> findByWorkId(Long workId);
}
