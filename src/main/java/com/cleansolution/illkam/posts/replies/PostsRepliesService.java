package com.cleansolution.illkam.posts.replies;

import com.cleansolution.illkam.firebase.FCMService;
import com.cleansolution.illkam.posts.Posts;
import com.cleansolution.illkam.posts.PostsRepository;
import com.cleansolution.illkam.posts.categories.PostsCategoriesRepository;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesSaveRequestDto;
import com.cleansolution.illkam.posts.replies.dto.PostsRepliesSaveRequestDto;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsRepliesService {
    private final PostsRepliesRepository postsRepliesRepository;
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final FCMService fcmService;

    @Transactional
    public Long save(PostsRepliesSaveRequestDto requestDto) {
        Users user = usersRepository.findById(requestDto.getUser_id()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + requestDto.getUser_id()));
        Posts post = postsRepository.findById(requestDto.getPost_id()).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + requestDto.getPost_id()));
        requestDto.updateUsersAndPosts(user, post);
        try {
            fcmService.sendMessage(
                    post.getWriter(),
                    "게시글 답글",
                    "작성하신 게시글에 답글이 달렸어요.",
                    "post", post.getId().toString()
            );
        } catch (Exception e) {
            System.out.println(e);
        }
        return postsRepliesRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long replyQna(PostsRepliesSaveRequestDto requestDto) {
        Posts post = postsRepository.findById(requestDto.getPost_id()).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + requestDto.getPost_id()));

        PostsReplies replies = PostsReplies.builder()
                .contents(requestDto.getContents())
                .post(post)
                .build();
        postsRepliesRepository.save(replies);
        try {
            fcmService.sendMessage(post.getWriter(), "1:1 문의 답변", "작성하신 문의에 답글이 달렸어요.", "post", post.getId().toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        return postsRepliesRepository.save(requestDto.toEntity()).getId();
    }


}
