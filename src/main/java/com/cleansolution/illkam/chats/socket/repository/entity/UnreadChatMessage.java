package com.cleansolution.illkam.chats.socket.repository.entity;

import com.cleansolution.illkam.users.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
public class UnreadChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users reader;

    @ManyToOne
    private ChatRoom chatRoom;


    @ColumnDefault("0")
    @Setter
    private int unreadCount = 0;

    public UnreadChatMessage(Users receiver, ChatRoom chatRoom) {
        this.reader =receiver;
        this.chatRoom = chatRoom;
    }
    // Getters and Setters

}
