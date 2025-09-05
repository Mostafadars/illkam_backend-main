package com.cleansolution.illkam.users.dto;

import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkReviews.dto.WorkReviewDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Getter
public class UsersResponseDto {
    private Long id;
    private String businessAddress;
    private String businessNumber;
    private String phoneNumber;
    private String email;
    private  String name;
    private Double rating;
    private Integer reviewCount;
    private List<WorkReviewDto> reviews;
    private String businessCertification;
    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;

    public UsersResponseDto(Users entity, List<WorkReviews> rev){
        this.id = entity.getId();
        this.name = entity.getName();
        this.businessAddress = entity.getBusinessAddress();
        this.businessNumber = entity.getBusinessNumber();
        this.phoneNumber = entity.getPhoneNumber();
        this.rating =entity.getRating();
        this.reviewCount =entity.getReviewCount();
        this.businessCertification = entity.getBusinessCertification();
        this.email = entity.getEmail();
        this.createdDateTime = entity.getCreatedDateTime();
        this.modifiedDateTime = entity.getModifiedDateTime();

        this.reviews = rev.stream().map(this::convertTOWorkReviewDto).collect(Collectors.toList());
    }

    private WorkReviewDto convertTOWorkReviewDto(WorkReviews reviews){
        return new WorkReviewDto(reviews);
    }

}
