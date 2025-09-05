package com.cleansolution.illkam.posts;

import com.cleansolution.illkam.posts.categories.PostsCategories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository  extends JpaRepository<Posts, Long> {

    Page<Posts> findAll(Pageable pageable);

    Page<Posts> findByCategory(PostsCategories category, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.category.id NOT IN :categoryIds")
    Page<Posts> findAllByCategoryExcept(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

//    List<Posts> findByCategorySorted(PostsCategories category, Pageable pageable);

}
