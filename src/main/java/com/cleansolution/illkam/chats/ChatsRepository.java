package com.cleansolution.illkam.chats;

import com.cleansolution.illkam.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatsRepository extends JpaRepository<Chats, Long> {

//    내가 등록한 일깜, 내가 신청한일깜 조회시
    List<Chats> findAllByEmployeeId(Long employee);
    List<Chats> findAllByEmployerId(Long employer);
//    채팅방 만들 때 로직
    Optional<Chats> findByWorkIdAndEmployeeId(Long workId, Long employeeId);
    Optional<Chats> findByWorkIdAndEmployerId(Long workId, Long employerId);
    Optional<Chats> findByWorkIdAndEmployerIdAndEmployeeId(Long workId, Long employerId, Long employeeId);
}
