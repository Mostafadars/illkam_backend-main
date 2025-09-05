package com.cleansolution.illkam.posts.categories;

import com.cleansolution.illkam.posts.Posts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class PostsCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST )
    @JsonIgnore
    private List<Posts> posts = new ArrayList<>();

    @Builder
    public PostsCategories(String name){
        this.name = name;
    }

}
