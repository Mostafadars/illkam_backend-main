package com.cleansolution.illkam.posts.replies.dto;

import com.cleansolution.illkam.posts.Posts;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.posts.replies.PostsReplies;
import com.cleansolution.illkam.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsRepliesSaveRequestDto {
    private String contents;
    private Posts post;

    private Long post_id;

    private Users user;
    private Long user_id;

    @Builder
    public PostsRepliesSaveRequestDto(String contents, Long post_id, Long user_id) {
        this.contents = contents;
        this.user_id = user_id;
        this.post_id =post_id;
    }
    public void updateUsersAndPosts(Users user, Posts post){
        this.user =user;
        this.post = post;
    }

    public PostsReplies toEntity() {
        return PostsReplies.builder()
                .contents(contents)
                .writer(user)
                .post(post)
                .build();
    }
}
