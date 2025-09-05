package com.cleansolution.illkam.posts.dto;

import com.cleansolution.illkam.base.BaseDto;
import com.cleansolution.illkam.posts.Posts;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.posts.images.PostsImages;
import com.cleansolution.illkam.posts.replies.PostsReplies;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.images.WorkImages;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto extends BaseDto {
    private Long id;
    private String title;
    private String contents;
    private Users writer;
    private PostsCategories category;
    private List<String> images;
    private List<PostsReplies> replies;

    public PostsResponseDto(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
        this.writer = entity.getWriter();
        this.category = entity.getCategory();
        this.images = entity.getImages().stream().map(this::convertToDTO).collect(Collectors.toList());
        this.replies = entity.getReplies();
        super.setCreatedDateTime(entity.getCreatedDateTime());
        super.setModifiedDateTime(entity.getModifiedDateTime());
    }

    private String convertToDTO(PostsImages postsImages) {
        return postsImages.getPhotoURL();
    }
}
