package com.cleansolution.illkam.chats.socket.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ChatMessage")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "roomId", insertable = false, updatable = false)
    private String roomId; //단순히 ID 값만 필요  (ChatRoom)

    @JoinColumn(name = "authorId", insertable = false, updatable = false)
    private String authorId; //단순히 ID 값만 필요 (User)

    @JoinColumn(name = "receiverId", insertable = false, updatable = false)
    private String receiverId; //단순히 ID 값만 필요 (User)

    @Column(name = "message")
    private String message;

    @Column(length = 1, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    @Builder.Default
    private Integer readCount = 1;

    @Column(name = "createdAt", updatable = false)
    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;

}
