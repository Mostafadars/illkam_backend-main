package com.cleansolution.illkam.posts.replies;

import com.cleansolution.illkam.posts.categories.PostsCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepliesRepository  extends JpaRepository<PostsReplies, Long> {
}
