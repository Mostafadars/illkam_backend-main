package com.cleansolution.illkam.posts;

import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.posts.categories.PostsCategoriesRepository;
import com.cleansolution.illkam.posts.dto.PostsResponseDto;
import com.cleansolution.illkam.posts.dto.PostsSaveRequestDto;
import com.cleansolution.illkam.posts.images.PostsImages;
import com.cleansolution.illkam.posts.images.PostsImagesRepository;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final PostsCategoriesRepository postsCategoriesRepository;
    private final PostsImagesRepository postsImagesRepository;


    @Transactional
    public void edit(Long postid, PostsSaveRequestDto requestDto){
        Posts post =  postsRepository.findById(postid).orElseThrow(()-> new IllegalArgumentException("post가 존재하지 않음"));
        Users user = usersRepository.findById(requestDto.getWriter()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+requestDto.getWriter()));;

//        게시글 카테고리 변경
        PostsCategories category = postsCategoriesRepository.findById(requestDto.getCategory_id()).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id="+requestDto.getCategory_id()));;
        requestDto.updateWriterAndCategory(user, category);
//        게시글에 정보 업데이트
        post.update(requestDto.getTitle(), requestDto.getContents(), requestDto.getCategory() );
        postsRepository.save(post);

//        기존 사진들 삭제
        postsImagesRepository.deleteAllByPostId(post.getId());

        List<PostsImages> postsImages = new ArrayList<>();
        for(String imageURL : requestDto.getImages()){
            PostsImages postImage = PostsImages.builder()
                    .photoURL(imageURL)
                    .post(post)
                    .build();
            postsImages.add(postImage);
        }
        postsImagesRepository.saveAll(postsImages);
    }

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        Users user = usersRepository.findById(requestDto.getWriter()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+requestDto.getWriter()));;
        PostsCategories category = postsCategoriesRepository.findById(requestDto.getCategory_id()).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id="+requestDto.getCategory_id()));;
        requestDto.updateWriterAndCategory(user, category);
        Posts posts= postsRepository.save(requestDto.toEntity());
        List<PostsImages> postsImages = new ArrayList<>();
        for(String imageURL : requestDto.getImages()){
            PostsImages postImage = PostsImages.builder()
                    .photoURL(imageURL)
                    .post(posts)
                    .build();
            postsImages.add(postImage);
        }
        postsImagesRepository.saveAll(postsImages);

        return posts.getId();
    }

    @Transactional
    public PostsResponseDto editNotice(PostsSaveRequestDto requestDto, Long postId){
        Posts posts = postsRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("해당 포스트가 없습니다."));
        posts.updateNotice(requestDto.getTitle(), requestDto.getContents());
        postsImagesRepository.deleteAllByPostId(postId);
        List<PostsImages> postsImages = new ArrayList<>();
        for(String imageURL : requestDto.getImages()){
            PostsImages postImage = PostsImages.builder()
                    .photoURL(imageURL)
                    .post(posts)
                    .build();
            postsImages.add(postImage);
        }
        postsImagesRepository.saveAll(postsImages);
        return convertToDTO(posts);
    }

    @Transactional
    public Boolean delete(Long postId){
        postsRepository.deleteById(postId);
        return true;
    }


    @Transactional
    public PostsResponseDto findById (Long id){

        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PostsResponseDto(entity);
    }

    public Page<PostsResponseDto> findAll (Optional<Long> category, Pageable pageable){
        if(category.isEmpty() || category.get() == 1){
            Page<Posts> postsPage = postsRepository.findAll(pageable);
            List<PostsResponseDto> postsResponseDtos = postsPage.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return new PageImpl<>(postsResponseDtos, pageable, postsPage.getTotalElements());
        }else{
            PostsCategories postsCategories = postsCategoriesRepository.findById(category.get()).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다. id="+category));
            Page<Posts> postsPage = postsRepository.findByCategory(postsCategories,pageable);
            List<PostsResponseDto> postsResponseDtos = postsPage.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return new PageImpl<>(postsResponseDtos, pageable, postsPage.getTotalElements());

        }
    }

//    for 어드민, 모든 게시글 , 공지사항, 1:1문의 제외하고 가져오기
    public Page<PostsResponseDto> findAllPublicArticle (Pageable pageable){

        Page<Posts> postsPage = postsRepository.findAllByCategoryExcept(List.of(2L, 7L),pageable);
        List<PostsResponseDto> postsResponseDtos = postsPage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(postsResponseDtos, pageable, postsPage.getTotalElements());
    }

    private PostsResponseDto convertToDTO(Posts post) {
        return new PostsResponseDto(post);
    }



}
