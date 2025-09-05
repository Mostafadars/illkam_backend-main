package com.cleansolution.illkam.posts;

import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.posts.categories.PostsCategoriesService;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesResponseDto;
import com.cleansolution.illkam.posts.categories.dto.PostsCategoriesSaveRequestDto;
import com.cleansolution.illkam.posts.dto.PostsResponseDto;
import com.cleansolution.illkam.posts.dto.PostsSaveRequestDto;
import com.cleansolution.illkam.posts.replies.PostsRepliesService;
import com.cleansolution.illkam.posts.replies.dto.PostsRepliesSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/admin/posts")
public class PostsAdminController {

    private final PostsService postsService;
    private final PostsCategoriesService postsCategoriesService;
    private final PostsRepliesService postsRepliesService;

    //    어드민 기능

    //    TODO 공지사항 확인 기능 -> 작성 시 모두에게 알람 가야 함
    @GetMapping("/notice")
    public Page<PostsResponseDto> getNotice(@RequestParam int page) {
        PostsCategories categories = postsCategoriesService.getCategory("공지사항");
        Sort sort = Sort.by("createdDateTime").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return postsService.findAll(Optional.ofNullable(categories.getId()), pageable);
    }

    @PostMapping("/notice")
    public Long writeNotice(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/notice/{postId}")
    PostsResponseDto editNotice(@RequestBody PostsSaveRequestDto requestDto, @PathVariable Long postId) {
        return postsService.editNotice(requestDto, postId);
    }

    @DeleteMapping("/notice/{postId}")
    Boolean deleteNotice(@PathVariable Long postId) {
        return postsService.delete(postId);
    }

    //    TODO 1:1문의 답변 기능 -> 작성 시 작성자에게 알람 가야 함
    @GetMapping("/qna")
    public Page<PostsResponseDto> getQna(@RequestParam int page) {
        PostsCategories categories = postsCategoriesService.getCategory("1:1 문의");
        Sort sort = Sort.by("createdDateTime").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return postsService.findAll(Optional.ofNullable(categories.getId()), pageable);
    }

    @PostMapping("/qna")
    public Long answerQna(@RequestBody PostsRepliesSaveRequestDto requestDto) {
        return postsRepliesService.replyQna(requestDto);
    }

    //    TODO 게시글 관리 기능 ->
    @GetMapping("/articles")
    public Page<PostsResponseDto> getArticles(@RequestParam int page) {
        Sort sort = Sort.by("createdDateTime").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return postsService.findAllPublicArticle(pageable);
    }

    @DeleteMapping("/articles/{postId}")
    Boolean deleteArticles(@PathVariable Long postId) {
        return postsService.delete(postId);
    }

    //    TODO 게시글 카테고리 추가
    @GetMapping("/category")
    public List<PostsCategoriesResponseDto> getCategory() {
        return postsCategoriesService.findAll();
    }

    @PostMapping("/category")
    public Long addCategory(@RequestBody PostsCategoriesSaveRequestDto requestDto) {
        return postsCategoriesService.save(requestDto);
    }

    @DeleteMapping("/category/{categoryId}")
    Boolean deleteCategory(@PathVariable Long categoryId) {
        return postsCategoriesService.delete(categoryId);
    }
}
