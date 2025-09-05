package com.cleansolution.illkam.posts.images;

import com.cleansolution.illkam.posts.Posts;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@Getter
@SuperBuilder
public class PostsImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoURL;

    @ManyToOne
    @JoinColumn(name = "post")
    private Posts post;

}
