package com.cleansolution.illkam.firebase;

import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import com.cleansolution.illkam.firebase.entity.Notifications;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final NotificationsRepository notificationsRepository;
    private final UsersRepository usersRepository;

    @Async
    public void sendMessage(Users user, String title, String body, String routeName, Long targetPageId) throws Exception {
        // FCM 메시지 작성
        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("routeName", routeName)
                .putData("targetPageId", String.valueOf(targetPageId))
                .build();

        // 메시지 전송
        String response = FirebaseMessaging.getInstance().send(message);
        notificationsRepository.save(Notifications.builder()
                .body(body)
                .title(title)
                .routeName(routeName)
                .targetPageId(String.valueOf(targetPageId))
                .user(user)
                .build());
        System.out.println("Successfully sent message: " + response);
    }

    @Async
    public void sendChatFcm(ChatMessage msg) throws Exception {
        Users recevier= usersRepository.findById(Long.valueOf(msg.getReceiverId())).orElseThrow(()-> new IllegalArgumentException("유저가 존재하지 않습니다. "));
        Users author= usersRepository.findById(Long.valueOf(msg.getAuthorId())).orElseThrow(()-> new IllegalArgumentException("유저가 존재하지 않습니다. "));

        // FCM 메시지 작성
        Message message = Message.builder()
                .setToken(recevier.getFcmToken())
                .setNotification(Notification.builder()
                        .setTitle(author.getName())
                        .setBody(msg.getMessage())
                        .build())
                .putData("routeName", "chats")
                .putData("targetPageId", msg.getRoomId())
                .build();

        // 메시지 전송
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }

    public List<Notifications> getUserNotifications(Long id) throws Exception {
        Sort sort = Sort.by("createdDateTime").descending();
        // FCM 메시지 작성
        return notificationsRepository.findByUserId(id,sort);
    }

    public Boolean sendMessage(Long id, FCMRequestDto dto) throws Exception {
        // FCM 메시지 작성
        try {
            Users user = usersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

            System.out.println(user.getName());
            sendMessage(user, dto.getTitle(), dto.getBody(), dto.getRouteName(), Long.valueOf(dto.getTargetPageId()));
            return true;
        } catch (Exception e) {
            return false;
        }


    }

//    @Transactional
//    public List<Notifications> getUserNotifications(Long id) throws Exception {
//        // FCM 메시지 작성
//        return notificationsRepository.findByUserId(id);
//    }
}
