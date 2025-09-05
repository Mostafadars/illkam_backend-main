package com.cleansolution.illkam.posts.categories;

import com.cleansolution.illkam.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostsCategoriesRepository  extends JpaRepository<PostsCategories, Long> {

    PostsCategories findOneById(Long id);

    Optional<PostsCategories> findByName(String name);
}
