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
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.WorksRepository;
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
    private final WorksRepository worksRepository;

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


//    // New Feature 12: Information exposure restriction and chatroom entry filtering
//    public UsersResponseDto getUserInfo(UserInfoRequestDto requestDto) {
//        // Validate that user1 exists
//        Users user1 = usersRepository.findById(requestDto.getUser1Id())
//                .orElseThrow(() -> new IllegalArgumentException("요청하는 유저가 없습니다."));
//
//        // If user2Id is null, return user1's full info
//        if (requestDto.getUser2Id() == null) {
//            List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(user1);
//            return new UsersResponseDto(user1, reviews);
//        }
//
//        // Validate that user2 exists
//        Users user2 = usersRepository.findById(requestDto.getUser2Id())
//                .orElseThrow(() -> new IllegalArgumentException("정보를 요청하는 유저가 없습니다."));
//
//        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(user2);
//        UsersResponseDto responseDto = new UsersResponseDto(user2, reviews);
//
//        // If workId is null, return user2's info with sensitive data hidden
//        if (requestDto.getWorkId() == null) {
//            return hideSensitiveData(responseDto);
//        }
//
//        // If workId is provided, check if user1 is confirmed for this work
//        Works work = worksRepository.findById(requestDto.getWorkId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 일깜이 존재하지 않습니다."));
//
//        // Check if user1 has a confirmed apply for this work
//        boolean isConfirmed = work.getAppliesList().stream()
//                .anyMatch(apply -> apply.getApplier().getId().equals(requestDto.getUser1Id())
//                        && apply.getStatus() == 1);
//
//        if (isConfirmed) {
//            // Return full user2 data
//            return responseDto;
//        } else {
//            // Return user2 data with sensitive data hidden
//            return hideSensitiveData(responseDto);
//        }
//    }
//
//    // Helper method to hide sensitive data
//    private UsersResponseDto hideSensitiveData(UsersResponseDto dto) {
//
//        dto.setBusinessNumber(null);
//        dto.setPhoneNumber(null);
//
//        return dto;
//    }
//

//    // New Feature 12: Information exposure restriction and chatroom entry filtering
//    public UsersResponseDto getUserInfo(UserInfoRequestDto requestDto) {
//        System.out.println("==> Enter getUserInfo service");
//
//        // Validate that user1 exists
//        Users user1 = usersRepository.findById(requestDto.getUser1Id())
//                .orElseThrow(() -> new IllegalArgumentException("요청하는 유저가 없습니다."));
//
//        System.out.println("Found " + user1.getId() + " user");
//
//        // If user2Id is null, return user1's full info
//        if (requestDto.getUser2Id() == null) {
//            List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(user1);
//            return new UsersResponseDto(user1, reviews);
//        }
//
//        // Validate that user2 exists
//        Users user2 = usersRepository.findById(requestDto.getUser2Id())
//                .orElseThrow(() -> new IllegalArgumentException("정보를 요청하는 유저가 없습니다."));
//
//        System.out.println("Found " + user2.getId() + " user");
//
//        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(user2);
//        UsersResponseDto responseDto = new UsersResponseDto(user2, reviews);
//
//        // If workId is null, return user2's info with sensitive data hidden
//        if (requestDto.getWorkId() == null) {
//            return hideSensitiveData(responseDto);
//        }
//
//        // If workId is provided, check the permission
//        Works work = worksRepository.findById(requestDto.getWorkId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 일깜이 존재하지 않습니다."));
//
//        System.out.println("Found " + work.getId() + " work");
//
//        // First, check if user1 is the employer of this work
//        boolean isEmployer = work.getEmployer().getId().equals(requestDto.getUser1Id());
//
//        System.out.println("Found " + isEmployer + " work");
//
//        if (isEmployer) {
//            // User1 is the employer - return full user2 data
//            System.out.println("Found employer work");
//            return responseDto;
//        }
//
//        // If not employer, check if user1 has a confirmed apply for this work
//        boolean isConfirmed = work.getAppliesList().stream()
//                .anyMatch(apply -> apply.getApplier().getId().equals(requestDto.getUser1Id())
//                        && apply.getStatus() == 1);
//
//        System.out.println("Found " + isConfirmed + " work");
//
//        if (isConfirmed) {
//            // Return full user2 data (user1 is confirmed employee)
//            System.out.println("Found confirmed work");
//            return responseDto;
//        } else {
//            // User1 is neither employer nor confirmed - return sensitive data hidden
//            System.out.println("Found not confirmed work");
//            return hideSensitiveData(responseDto);
//        }
//    }
//
//    // Helper method to hide sensitive data
//    private UsersResponseDto hideSensitiveData(UsersResponseDto dto) {
//        System.out.println("==> Enter hideSensitiveData service");
//        System.out.println("Hide data of " + dto.getId() + " user");
//        dto.setBusinessNumber(null);
//        dto.setPhoneNumber(null);
//        return dto;
//    }

    // New Feature 12: Information exposure restriction and chatroom entry filtering
    public UsersResponseDto getUserInfo(UserInfoRequestDto requestDto) {
        System.out.println("==> Enter getUserInfo service");

        // Validate that user1 exists
        Users user1 = usersRepository.findById(requestDto.getUser1Id())
                .orElseThrow(() -> new IllegalArgumentException("요청하는 유저가 없습니다."));

        System.out.println("Found " + user1.getId() + " user");

        // If user2Id is null, return user1's full info
        if (requestDto.getUser2Id() == null) {
            List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(user1);
            return new UsersResponseDto(user1, reviews);
        }

        // Validate that user2 exists
        Users user2 = usersRepository.findById(requestDto.getUser2Id())
                .orElseThrow(() -> new IllegalArgumentException("정보를 요청하는 유저가 없습니다."));

        System.out.println("Found " + user2.getId() + " user");

        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(user2);
        UsersResponseDto responseDto = new UsersResponseDto(user2, reviews);

        // If workId is null, return user2's info with sensitive data hidden
        if (requestDto.getWorkId() == null) {
            return hideSensitiveData(responseDto);
        }

        // If workId is provided, check the permission
        Works work = worksRepository.findById(requestDto.getWorkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 일깜이 존재하지 않습니다."));

        System.out.println("Found " + work.getId() + " work");

        // First, check if user1 is the employer of this work AND work is confirmed
        boolean isEmployerAndConfirmed = work.getEmployer().getId().equals(requestDto.getUser1Id())
                && Boolean.TRUE.equals(work.getIsConfirmed());

        System.out.println("Is employer and confirmed: " + isEmployerAndConfirmed);

        if (isEmployerAndConfirmed) {
            // User1 is the employer AND work is confirmed - return full user2 data
            System.out.println("Found employer and confirmed work");
            return responseDto;
        }

        // If not employer or work not confirmed, check if user1 has a confirmed apply for this work
        boolean isConfirmed = work.getAppliesList().stream()
                .anyMatch(apply -> apply.getApplier().getId().equals(requestDto.getUser1Id())
                        && apply.getStatus() == 1);

        System.out.println("Found " + isConfirmed + " work");

        if (isConfirmed) {
            // Return full user2 data (user1 is confirmed employee)
            System.out.println("Found confirmed work");
            return responseDto;
        } else {
            // User1 is neither confirmed employer nor confirmed employee - return sensitive data hidden
            System.out.println("Found not confirmed work");
            return hideSensitiveData(responseDto);
        }
    }

    // Helper method to hide sensitive data
    private UsersResponseDto hideSensitiveData(UsersResponseDto dto) {
        System.out.println("==> Enter hideSensitiveData service");
        System.out.println("Hide data of " + dto.getId() + " user");
        dto.setBusinessNumber(null);
        dto.setPhoneNumber(null);
        return dto;
    }

}
