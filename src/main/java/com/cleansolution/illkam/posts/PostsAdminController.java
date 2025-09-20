package com.cleansolution.illkam.posts;

import com.cleansolution.illkam.firebase.FCMRequestDto;
import com.cleansolution.illkam.firebase.FCMService;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.posts.categories.PostsCategoriesService;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesResponseDto;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesSaveRequestDto;
import com.cleansolution.illkam.posts.dto.PostsResponseDto;
import com.cleansolution.illkam.posts.dto.PostsSaveRequestDto;
import com.cleansolution.illkam.posts.replies.PostsRepliesService;
import com.cleansolution.illkam.posts.replies.dto.PostsRepliesSaveRequestDto;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/admin/posts")
public class PostsAdminController {

    private final PostsService postsService;
    private final PostsCategoriesService postsCategoriesService;
    private final PostsRepliesService postsRepliesService;
    private final UsersRepository usersRepository;
    private final FCMService fcmService;

    //    어드민 기능

    //    TODO 공지사항 확인 기능 -> 작성 시 모두에게 알람 가야 함
    @GetMapping("/notice")
    public Page<PostsResponseDto> getNotice(@RequestParam int page) {
        PostsCategories categories = postsCategoriesService.getCategory("공지사항");
        Sort sort = Sort.by("createdDateTime").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return postsService.findAll(Optional.ofNullable(categories.getId()), pageable);
    }

    @PostMapping("/notice")
    public Long writeNotice(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    // New Feature 1: Admin Posts and Notifications
    @PostMapping("/notice/{option}")
    public Long writeNotice(@PathVariable int option, @RequestBody PostsSaveRequestDto requestDto) {
        // Get marketing consent users for options 2 and 3
        List<Users> marketingConsentUsers = null;
        if (option == 2 || option == 3) {
            marketingConsentUsers = usersRepository.findByMarketingConsentTrue();
        }

        // Option 1: Save post only (regular logic)
        if (option == 1) {
            return postsService.save(requestDto);
        }

        // Option 2: Send notification only (no post saving)
        else if (option == 2) {
            sendNotificationsToMarketingUsers(marketingConsentUsers, requestDto);
            return -1L; // Return a dummy ID since no post is saved
        }

        // Option 3: Save post AND send notifications
        else if (option == 3) {
            Long postId = postsService.save(requestDto);
            sendNotificationsToMarketingUsers(marketingConsentUsers, requestDto);
            return postId;
        }

        // Invalid option
        else {
            throw new IllegalArgumentException("Invalid option value. Must be 1, 2, or 3.");
        }
    }

//    private void sendNotificationsToMarketingUsers(List<Users> marketingUsers, PostsSaveRequestDto requestDto) {
//        if (marketingUsers == null || marketingUsers.isEmpty()) {
//            return;
//        }
//
//        for (Users user : marketingUsers) {
//            try {
//                // Create FCM request DTO
//                FCMRequestDto fcmRequest = FCMRequestDto.builder()
//                        .title(requestDto.getTitle())
//                        .body(requestDto.getContents())
//                        .routeName("notice")
//                        .targetPageId("") // Empty or appropriate value for notice
//                        .build();
//
//                // Send notification
//                fcmService.sendMessage(user.getId(), fcmRequest);
//            } catch (Exception e) {
//                // Log error but continue with other users
//                System.err.println("Failed to send notification to user: " + user.getId() + ", Error: " + e.getMessage());
//            }
//        }
//    }

    private void sendNotificationsToMarketingUsers(List<Users> marketingUsers, PostsSaveRequestDto requestDto) {
        if (marketingUsers == null || marketingUsers.isEmpty()) {
            System.out.println("No marketing consent users found.");
            return;
        }

        for (Users user : marketingUsers) {
            try {
                // Check if user has FCM token
                if (user.getFcmToken() == null || user.getFcmToken().trim().isEmpty()) {
                    System.out.println("Skipping user " + user.getId() + " (" + user.getName() + "): No FCM token available");
                    continue;
                }

                // Check if token looks valid (basic validation)
                if (!isValidFcmToken(user.getFcmToken())) {
                    System.out.println("Skipping user " + user.getId() + " (" + user.getName() + "): Invalid FCM token format");
                    continue;
                }

                // Create FCM request DTO
                FCMRequestDto fcmRequest = FCMRequestDto.builder()
                        .title(requestDto.getTitle())
                        .body(requestDto.getContents())
                        .routeName("notice")
                        .targetPageId("") // Empty or appropriate value for notice
                        .build();

                // Send notification
                fcmService.sendMessage2(user.getId(), fcmRequest);
                System.out.println("Notification sent successfully to user " + user.getId() + " (" + user.getName() + ")");

            } catch (Exception e) {
                // Handle specific FCM errors
                if (e.getMessage() != null) {
                    if (e.getMessage().contains("Exactly one of token, topic or condition must be specified")) {
                        System.out.println("Skipping user " + user.getId() + " (" + user.getName() + "): Invalid or missing FCM token");
                    } else if (e.getMessage().contains("Requested entity was not found") ||
                            e.getMessage().contains("UNREGISTERED")) {
                        System.out.println("Skipping user " + user.getId() + " (" + user.getName() + "): FCM token is expired or unregistered");
                    } else {
                        System.out.println("Failed to send notification to user " + user.getId() + " (" + user.getName() + "): " + e.getMessage());
                    }
                } else {
                    System.out.println("Failed to send notification to user " + user.getId() + " (" + user.getName() + "): Unknown error");
                }
            }
        }
    }

    // Helper method to validate FCM token format
    private boolean isValidFcmToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        // Basic validation: FCM tokens should be long strings with colons
        // Actual format: usually starts with the project ID or contains multiple segments
        return token.length() > 50 && token.contains(":");
    }

    @PutMapping("/notice/{postId}")
    PostsResponseDto editNotice(@RequestBody PostsSaveRequestDto requestDto, @PathVariable Long postId) {
        return postsService.editNotice(requestDto, postId);
    }

    @DeleteMapping("/notice/{postId}")
    Boolean deleteNotice(@PathVariable Long postId) {
        return postsService.delete(postId);
    }

    //    TODO 1:1문의 답변 기능 -> 작성 시 작성자에게 알람 가야 함
    @GetMapping("/qna")
    public Page<PostsResponseDto> getQna(@RequestParam int page) {
        PostsCategories categories = postsCategoriesService.getCategory("1:1 문의");
        Sort sort = Sort.by("createdDateTime").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return postsService.findAll(Optional.ofNullable(categories.getId()), pageable);
    }

    @PostMapping("/qna")
    public Long answerQna(@RequestBody PostsRepliesSaveRequestDto requestDto) {
        return postsRepliesService.replyQna(requestDto);
    }

    //    TODO 게시글 관리 기능 ->
    @GetMapping("/articles")
    public Page<PostsResponseDto> getArticles(@RequestParam int page) {
        Sort sort = Sort.by("createdDateTime").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return postsService.findAllPublicArticle(pageable);
    }

    @DeleteMapping("/articles/{postId}")
    Boolean deleteArticles(@PathVariable Long postId) {
        return postsService.delete(postId);
    }

    //    TODO 게시글 카테고리 추가
    @GetMapping("/category")
    public List<PostsCategoriesResponseDto> getCategory() {
        return postsCategoriesService.findAll();
    }

    @PostMapping("/category")
    public Long addCategory(@RequestBody PostsCategoriesSaveRequestDto requestDto) {
        return postsCategoriesService.save(requestDto);
    }

    @DeleteMapping("/category/{categoryId}")
    Boolean deleteCategory(@PathVariable Long categoryId) {
        return postsCategoriesService.delete(categoryId);
    }
}
