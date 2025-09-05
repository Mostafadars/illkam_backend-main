package com.cleansolution.illkam.posts.dto;

import com.cleansolution.illkam.posts.Posts;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String contents;
    private Long writer;
    private Long category_id;
    private PostsCategories category;
    private Users user;
    private List<String> images;

    @Builder
    public PostsSaveRequestDto(String title,
                               String contents, Long writer, Long category_id, List<String> images) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.category_id =category_id;
        this.images = images;
    }
//    asdf
    public void updateWriterAndCategory(Users user, PostsCategories category){
        this.user =user;
        this.category = category;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .contents(contents)
                .writer(user)
                .category(category)
                .build();
    }
}
