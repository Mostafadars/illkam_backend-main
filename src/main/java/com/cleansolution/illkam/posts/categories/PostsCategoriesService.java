package com.cleansolution.illkam.posts.categories;

import com.cleansolution.illkam.posts.Posts;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesResponseDto;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesSaveRequestDto;
import com.cleansolution.illkam.posts.dto.PostsResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsCategoriesService {
    private final PostsCategoriesRepository postsCategoriesRepository;

    @Transactional
    public Long save(PostsCategoriesSaveRequestDto requestDto){
        return postsCategoriesRepository.save(requestDto.toEntity()).getId();
    }

    public List<PostsCategoriesResponseDto> findAll(){
        return postsCategoriesRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean delete(Long postCategoryId){
        postsCategoriesRepository.deleteById(postCategoryId);
        return true;
    }

    public PostsCategories getCategory(String categoryName){
        return postsCategoriesRepository.findByName(categoryName).orElseThrow(()-> new IllegalArgumentException("카테고리가 존재하지 않음"));
    }

    private PostsCategoriesResponseDto convertToDTO(PostsCategories postsCategories) {
        return new PostsCategoriesResponseDto(postsCategories);
    }
}
