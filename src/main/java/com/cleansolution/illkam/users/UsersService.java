package com.cleansolution.illkam.users;

import com.cleansolution.illkam.chats.socket.repository.ChatMessageRepository;
import com.cleansolution.illkam.chats.socket.repository.ChatRoomRepository;
import com.cleansolution.illkam.chats.socket.repository.UnreadChatMessageRepository;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatMessage;
import com.cleansolution.illkam.chats.socket.repository.entity.ChatRoom;
import com.cleansolution.illkam.chats.socket.repository.entity.UnreadChatMessage;
import com.cleansolution.illkam.firebase.NotificationsRepository;
import com.cleansolution.illkam.firebase.entity.Notifications;
import com.cleansolution.illkam.users.dto.*;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkReviews.WorkReviewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final WorkReviewsRepository workReviewsRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UnreadChatMessageRepository unreadChatMessageRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationsRepository notificationsRepository;

    @Transactional
    public Long login(UserLoginDto loginDto){
        Users user = usersRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword()).orElseThrow(()-> new IllegalArgumentException());
        return user.getId();
    }

    @Transactional
    public Long save(UsersSaveRequestDto requestDto){
        System.out.println(requestDto);
        return usersRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long edit(Long id, UsersSaveRequestDto requestDto){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        entity.update(requestDto.getBusinessAddress(), requestDto.getPhoneNumber(),  requestDto.getEmail());

        return usersRepository.save(entity).getId();
    }

    @Transactional
    public Long updateFcm(Long id, UsersSaveRequestDto requestDto){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        entity.setFcmToken(requestDto.getFcmToken());
        return usersRepository.save(entity).getId();
    }

    public UsersResponseDto findById (Long id){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(entity);
        return new UsersResponseDto(entity,reviews);
    }

    public ApplierResponseDto findApplierById (Long id){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(entity);
        return new ApplierResponseDto(entity, reviews);
    }

    public EmployerResponseDto findEmployerById (Long id){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(entity);
        return new EmployerResponseDto(entity, reviews);
    }

    // Fix Delete User Bug with the chatroom
//    @Transactional
//    public Boolean deleteUser(Users user) {
//        // First, handle related chat rooms
//        List<ChatRoom> employeeRooms = chatRoomRepository.findAllByEmployeeIdOrderByModifiedDateTimeDesc(user.getId());
//        List<ChatRoom> employerRooms = chatRoomRepository.findAllByEmployerIdOrderByModifiedDateTimeDesc(user.getId());
//
//        for (ChatRoom chatRoom : employeeRooms) {
//            System.out.println("Processing chat room: " + chatRoom.getId());
//
//            // Delete UnreadChatMessage records first
//            List<UnreadChatMessage> unreadMessages = unreadChatMessageRepository.findByChatRoomId(chatRoom.getId());
//            System.out.println("Found " + unreadMessages.size() + " unread messages for chat room");
//            if (!unreadMessages.isEmpty()) {
//                unreadChatMessageRepository.deleteAll(unreadMessages);
//                System.out.println("Deleted unread messages");
//            }
//
//            // Delete ChatMessage records
//            List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(chatRoom.getId());
//            System.out.println("Found " + chatMessages.size() + " chat messages for chat room");
//            if (!chatMessages.isEmpty()) {
//                chatMessageRepository.deleteAll(chatMessages);
//                System.out.println("Deleted chat messages");
//            }
//        }
//
//        for (ChatRoom chatRoom : employerRooms) {
//            System.out.println("Processing chat room: " + chatRoom.getId());
//
//            // Delete UnreadChatMessage records first
//            List<UnreadChatMessage> unreadMessages = unreadChatMessageRepository.findByChatRoomId(chatRoom.getId());
//            System.out.println("Found " + unreadMessages.size() + " unread messages for chat room");
//            if (!unreadMessages.isEmpty()) {
//                unreadChatMessageRepository.deleteAll(unreadMessages);
//                System.out.println("Deleted unread messages");
//            }
//
//            // Delete ChatMessage records
//            List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(chatRoom.getId());
//            System.out.println("Found " + chatMessages.size() + " chat messages for chat room");
//            if (!chatMessages.isEmpty()) {
//                chatMessageRepository.deleteAll(chatMessages);
//                System.out.println("Deleted chat messages");
//            }
//        }
//
//        System.out.println("Deleting User...");
//        usersRepository.delete(user);
//
//        System.out.println("Delete completed successfully");
//        return true;
//    }

    @Transactional
    public Boolean deleteUser(Users user) {
        Long userId = user.getId();

        // First, handle related notifications
        List<Notifications> userNotifications = notificationsRepository.findByUserId(userId);
        System.out.println("Found " + userNotifications.size() + " notifications for user");
        if (!userNotifications.isEmpty()) {
            notificationsRepository.deleteAll(userNotifications);
            System.out.println("Deleted notifications");
        }

        // Then, handle related chat rooms
        List<ChatRoom> employeeRooms = chatRoomRepository.findAllByEmployeeIdOrderByModifiedDateTimeDesc(userId);
        List<ChatRoom> employerRooms = chatRoomRepository.findAllByEmployerIdOrderByModifiedDateTimeDesc(userId);

        for (ChatRoom chatRoom : employeeRooms) {
            System.out.println("Processing chat room: " + chatRoom.getId());

            // Delete UnreadChatMessage records first
            List<UnreadChatMessage> unreadMessages = unreadChatMessageRepository.findByChatRoomId(chatRoom.getId());
            System.out.println("Found " + unreadMessages.size() + " unread messages for chat room");
            if (!unreadMessages.isEmpty()) {
                unreadChatMessageRepository.deleteAll(unreadMessages);
                System.out.println("Deleted unread messages");
            }

            // Delete ChatMessage records
            List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(chatRoom.getId());
            System.out.println("Found " + chatMessages.size() + " chat messages for chat room");
            if (!chatMessages.isEmpty()) {
                chatMessageRepository.deleteAll(chatMessages);
                System.out.println("Deleted chat messages");
            }
        }

        for (ChatRoom chatRoom : employerRooms) {
            System.out.println("Processing chat room: " + chatRoom.getId());

            // Delete UnreadChatMessage records first
            List<UnreadChatMessage> unreadMessages = unreadChatMessageRepository.findByChatRoomId(chatRoom.getId());
            System.out.println("Found " + unreadMessages.size() + " unread messages for chat room");
            if (!unreadMessages.isEmpty()) {
                unreadChatMessageRepository.deleteAll(unreadMessages);
                System.out.println("Deleted unread messages");
            }

            // Delete ChatMessage records
            List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(chatRoom.getId());
            System.out.println("Found " + chatMessages.size() + " chat messages for chat room");
            if (!chatMessages.isEmpty()) {
                chatMessageRepository.deleteAll(chatMessages);
                System.out.println("Deleted chat messages");
            }
        }

        // Now delete the chat rooms themselves
        if (!employeeRooms.isEmpty()) {
            chatRoomRepository.deleteAll(employeeRooms);
            System.out.println("Deleted employee chat rooms");
        }
        if (!employerRooms.isEmpty()) {
            chatRoomRepository.deleteAll(employerRooms);
            System.out.println("Deleted employer chat rooms");
        }

        System.out.println("Deleting User...");
        usersRepository.delete(user);

        System.out.println("Delete completed successfully");
        return true;
    }
}
