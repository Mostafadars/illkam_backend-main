// package com.cleansolution.illkam.posts;

// import com.cleansolution.illkam.base.BaseTimestampEntity;
// import com.cleansolution.illkam.posts.categories.PostsCategories;
// import com.cleansolution.illkam.posts.images.PostsImages;
// import com.cleansolution.illkam.posts.replies.PostsReplies;
// import com.cleansolution.illkam.users.Users;
// import jakarta.persistence.*;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.experimental.SuperBuilder;

// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @NoArgsConstructor
// @Getter
// @SuperBuilder
// public class Posts extends BaseTimestampEntity {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.EAGER)
//     @JoinColumn(name = "category")
//     private PostsCategories category;

//     @Column(length = 20, nullable = false)
//     private String title;

//     @Column(length = 500, nullable = true)
//     private String contents;

//     @ManyToOne(fetch = FetchType.EAGER)
//     @JoinColumn(name = "writer")
//     private Users writer;

//     @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
//     private List<PostsImages> images = new ArrayList<>();

//     @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
//     private List<PostsReplies> replies = new ArrayList<>();

//     @Builder
//     public Posts(String title, String contents, Users writer, PostsCategories category) {
//         this.title = title;
//         this.contents = contents;
//         this.writer = writer;
//         this.category = category;
//     }

//     //  Entity 에 update 를 넣는 이유는 Entity Level 의 SQL 작업이라?
//     //    TODO 운영 방침 물어봐야 함. 한 번 쓴 글의 category 변경이 가능하게 할 것인지?
//     public void update(String title, String contents, PostsCategories category) {
//         this.title = title;
//         this.contents = contents;
//         this.category = category;
//     }

//     public void updateNotice(String title, String contents){
//         this.title =title;
//         this.contents = contents;
//     }
// }

package com.cleansolution.illkam.posts;

import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.posts.images.PostsImages;
import com.cleansolution.illkam.posts.replies.PostsReplies;
import com.cleansolution.illkam.users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@SuperBuilder // This will correctly generate a builder for the whole class
public class Posts extends BaseTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private PostsCategories category;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 500, nullable = true)
    private String contents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer")
    private Users writer;

    // This initialization is good practice! The builder will respect it.
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<PostsImages> images = new ArrayList<>();

    // This initialization is also good practice.
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<PostsReplies> replies = new ArrayList<>();

    // The constructor with @Builder has been removed to resolve the conflict.
    // @SuperBuilder will take care of generating the builder and the necessary
    // constructor.

    public void update(String title, String contents, PostsCategories category) {
        this.title = title;
        this.contents = contents;
        this.category = category;
    }

    public void updateNotice(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}