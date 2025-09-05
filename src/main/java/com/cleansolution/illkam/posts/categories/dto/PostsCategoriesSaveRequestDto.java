package com.cleansolution.illkam.posts.categories.dto;

import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsCategoriesSaveRequestDto {
    private String name;

    @Builder
    public PostsCategoriesSaveRequestDto(String name){
        this.name = name;
    }

    public PostsCategories toEntity(){
        return PostsCategories.builder()
                .name(name)
                .build();
    }
}
