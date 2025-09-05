package com.cleansolution.illkam.posts.images;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsImagesRepository extends JpaRepository<PostsImages, Long> {

    void deleteAllByPostId(Long postId);
}
