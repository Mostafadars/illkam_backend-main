package com.cleansolution.illkam.posts.categories.dto;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import lombok.Getter;

@Getter
public class PostsCategoriesResponseDto {

    private Long id;
    private String name;

    public PostsCategoriesResponseDto(PostsCategories entity){
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
