package com.cleansolution.illkam.chats.socket.repository;

import com.cleansolution.illkam.chats.socket.repository.entity.UnreadChatMessage;
import com.cleansolution.illkam.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnreadChatMessageRepository extends JpaRepository<UnreadChatMessage, Long> {
    Optional<UnreadChatMessage> findByReaderIdAndChatRoomId(Long reader, String chatRoomId);

    List<UnreadChatMessage> findByReaderId(Long userid);

}
