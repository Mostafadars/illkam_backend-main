package com.cleansolution.illkam.works.dto;

import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.Works;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WorkReviewSaveDto {
    private Long writer;
    private Long target;
    private String contents;
    private Integer starCount;
    private String image;

    @Builder
    public WorkReviewSaveDto(Long employer, Long employee, Long workId, Long writer, String contents, Integer starCount,String image) {
        this.writer = writer;
        this.contents = contents;
        this.starCount = starCount;
        this.image = image;
    }

    public WorkReviews toEntity(Users writer,Users target,  Works work) {
        return WorkReviews.builder()
                .work(work)
                .writer(writer)
                .work(work)
                .target(target)
                .contents(this.contents)
                .starCount(this.starCount)
                .image(this.image)
                .build();
    }
}
