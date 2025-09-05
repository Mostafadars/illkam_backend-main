package com.cleansolution.illkam.posts.categories;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class PostsDataInitializer {
    @Bean
    CommandLineRunner initializePostCategory(PostsCategoriesRepository postsCategoriesRepository) {
        return args -> {
            // WorkTypeDim 테이블이 비어 있으면 초기 데이터를 삽입
            if (postsCategoriesRepository.count() == 0) {
                List<PostsCategories> postsCategories = new ArrayList<PostsCategories>();
                PostsCategories all = PostsCategories
                        .builder()
                        .name("카테고리 선택")
                        .build();
                PostsCategories notice = PostsCategories
                        .builder()
                        .name("공지사항")
                        .build();
                PostsCategories used = PostsCategories
                        .builder()
                        .name("중고장터")
                        .build();
                PostsCategories tips = PostsCategories
                        .builder()
                        .name("나만의 노하우")
                        .build();
                PostsCategories freeArticles = PostsCategories
                        .builder()
                        .name("이런일 저런일")
                        .build();
                PostsCategories cs = PostsCategories
                        .builder()
                        .name("1:1 문의")
                        .build();
                PostsCategories hire = PostsCategories
                        .builder()
                        .name("구인구직")
                        .build();


                postsCategories.add(all);
                postsCategories.add(notice);
                postsCategories.add(used);
                postsCategories.add(tips);
                postsCategories.add(freeArticles);
                postsCategories.add(hire);
                postsCategories.add(cs);
                postsCategoriesRepository.saveAll(postsCategories);

                System.out.println("PostCategories 테이블에 초기 데이터를 삽입했습니다.");
            } else {
                System.out.println("PostCategories 테이블에 이미 데이터가 존재합니다.");
            }
        };
    }

}