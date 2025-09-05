package com.cleansolution.illkam.chats.socket.repository;

import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findAllByRoomId(String roomId, Pageable pageable);

    Optional<ChatMessage> findByRoomId(String roomId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChatMessage cm SET cm.readCount = 2 WHERE cm.roomId = :chatId AND cm.authorId <> :authorId AND cm.readCount = 1")
    void updateReadCountForMessages(String chatId, Long authorId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChatMessage cm SET cm.readCount = 2 WHERE cm.roomId = :chatId AND cm.readCount = 1 AND cm.receiverId = :readerId")
    void updateReadCountByChatId(String chatId, Long readerId);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChatMessage cm SET cm.readCount = 2 WHERE cm.id = :messageId AND cm.readCount = 1")
    void updateReadCountByMessageId(Long messageId);

}
