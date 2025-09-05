package com.cleansolution.illkam.posts.dto;

import com.cleansolution.illkam.posts.Posts;
import lombok.Builder;
import lombok.Getter;
@Getter
public class PostsFindRequestDto {
    private Integer count;
    private Long category;

    @Builder
    public PostsFindRequestDto(Integer count,
                               Long category) {
        this.count = count;
        this.category = category;
    }


}