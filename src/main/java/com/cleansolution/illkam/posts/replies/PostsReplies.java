package com.cleansolution.illkam.posts.replies;

import com.cleansolution.illkam.posts.Posts;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.users.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PostsReplies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Users writer;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "post")
    @JsonIgnore
    private Posts post;

    @Builder
    public PostsReplies(String contents, Users writer, Posts post) {
        this.contents = contents;
        this.writer = writer;
        this.post = post;
    }

}
