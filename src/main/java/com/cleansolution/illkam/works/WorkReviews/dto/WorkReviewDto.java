package com.cleansolution.illkam.works.WorkReviews.dto;

import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WorkReviewDto {
    private Long id;

    private Users employee;

    private Users employer;

    private Users writer;

    private Long work_id;

    private String contents;

    private String image;

    private Integer starCount;

    private LocalDateTime createdDateTime;

    public WorkReviewDto(WorkReviews entity){
        this.id = entity.getId();
        this.writer = entity.getWriter();
        this.work_id =entity.getWork().getId();
        this.contents = entity.getContents();
        this.image =entity.getImage();
        this.starCount = entity.getStarCount();
        this.createdDateTime = entity.getCreatedDateTime();
    }
}
