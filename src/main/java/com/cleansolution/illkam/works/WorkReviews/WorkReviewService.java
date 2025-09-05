package com.cleansolution.illkam.works.WorkReviews;

import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.dto.WorkReviewSaveDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkReviewService {
    private final WorkReviewsRepository workReviewsRepository;
    private final UsersRepository usersRepository;

    //
    @Transactional
    public Long writeReview(Works works, Users writer, Users target, WorkReviewSaveDto dto) {
//        1. 기존 리뷰 존재하는 지 확인하기
        Optional<WorkReviews> exReview = workReviewsRepository.findByWorkAndWriter(works, writer);

//        2. 리뷰 존재할 경우 업데이트, 평점의 경우 기존 count 활용
        if (exReview.isPresent()) {
            WorkReviews reviews = exReview.get();
//        기존 리뷰 업데이트
            reviews.update(dto.getContents(), dto.getStarCount(), dto.getImage());
            workReviewsRepository.save(exReview.get());
//        기존 유저의 starCount 업데이트
            target.calculateRating(dto.getStarCount(), target.getReviewCount());
            usersRepository.save(target);
            return exReview.get().getId();
        } else {
//            3. 없을 경우 새로 빌드해서 생성해주기
            WorkReviews workReviews = workReviewsRepository.save(dto.toEntity(writer, target, works));
            target.calculateRating(dto.getStarCount(), target.getReviewCount() + 1);
            writer.addWriteReview();
            return usersRepository.save(target).getId();
        }
    }

    @Transactional
    public Long editReview(WorkReviews workReviews, WorkReviewSaveDto requestDto){
        workReviews.update(requestDto.getContents(), requestDto.getStarCount(), requestDto.getImage());
        workReviewsRepository.save(workReviews);
        workReviews.getTarget().calculateRating(requestDto.getStarCount(), workReviews.getTarget().getReviewCount());
        usersRepository.save(workReviews.getTarget());
        return workReviews.getId();
    }

    public Boolean deleteReview(Long reviewId) {
        try {
//            리뷰 찾아와서 삭제하기
            System.out.println(reviewId);
            WorkReviews review = workReviewsRepository.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Review not found"));
            System.out.println(review.getContents());
            Users writer = review.getWriter();
            Users target = review.getTarget();
            writer.minusWriteReview();
            target.removeReview(review.getStarCount());
            usersRepository.save(writer);
            usersRepository.save(target);
            workReviewsRepository.deleteById(reviewId);
            workReviewsRepository.findById(reviewId);
//            작성자 게시 횟수 줄이고, 타겟은 리뷰 횟수 줄이기

        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }
}