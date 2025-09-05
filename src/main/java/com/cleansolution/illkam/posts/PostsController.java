package com.cleansolution.illkam.posts;

import com.cleansolution.illkam.posts.categories.PostsCategoriesRepository;
import com.cleansolution.illkam.posts.categories.PostsCategoriesService;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesResponseDto;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesSaveRequestDto;
import com.cleansolution.illkam.posts.dto.PostsResponseDto;
import com.cleansolution.illkam.posts.dto.PostsSaveRequestDto;
import com.cleansolution.illkam.posts.images.PostsImagesRepository;
import com.cleansolution.illkam.posts.replies.PostsRepliesService;
import com.cleansolution.illkam.posts.replies.dto.PostsRepliesSaveRequestDto;
import com.cleansolution.illkam.users.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostsController {
    private final PostsService postsService;
    private final PostsCategoriesService postsCategoriesService;
    private final PostsRepliesService postsRepliesService;
    private final PostsRepository postsRepository;
    private final PostsCategoriesRepository postsCategoriesRepository;
    private final UsersRepository usersRepository;
    private final PostsImagesRepository postsImagesRepository;

    @PostMapping("/")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @DeleteMapping("/{postid}")
    public Boolean remove(@PathVariable Long postid) {
        System.out.println(postid);
        postsRepository.deleteById(postid);
        return true;
    }

    @PutMapping("/{postid}")
    @Transactional
    public Boolean edit(@PathVariable Long postid, @RequestBody PostsSaveRequestDto requestDto) {
        postsService.edit(postid, requestDto);
        return true;
    }

    @GetMapping("/")
    public List<PostsResponseDto> findAll(
            @RequestParam Optional<Long> category, // 카테고리 필터링
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Sort sort = Sort.by("createdDateTime").descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return postsService.findAll(category, pageable).toList();
    }

    @GetMapping("/category")
    public List<PostsCategoriesResponseDto> findAllCategories() {
        return postsCategoriesService.findAll();
    }

    @GetMapping("/{id}")
    public PostsResponseDto findById(@PathVariable("id") Long id) {
        return postsService.findById(id);
    }

    @PostMapping("/category")
    public Long saveCategory(@RequestBody PostsCategoriesSaveRequestDto requestDto) {
        return postsCategoriesService.save(requestDto);
    }

    @PostMapping("/{id}/reply")
    public Long save(@RequestBody PostsRepliesSaveRequestDto requestDto) {
        return postsRepliesService.save(requestDto);
    }


}
