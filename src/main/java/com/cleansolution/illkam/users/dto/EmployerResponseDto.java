package com.cleansolution.illkam.users.dto;

import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkReviews.dto.WorkReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class EmployerResponseDto {
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


    public EmployerResponseDto(Users entity, List<WorkReviews> reviews){
        this.id = entity.getId();
        this.name = entity.getName();
        this.businessAddress = entity.getBusinessAddress();
        this.businessNumber = entity.getBusinessNumber();
        this.phoneNumber = entity.getPhoneNumber();
        this.rating =entity.getRating();
        this.reviewCount =entity.getReviewCount();
        this.businessCertification = entity.getBusinessCertification();
        this.email = entity.getEmail();
        this.reviews = reviews.stream().map(this::convertTOWorkReviewDto).collect(Collectors.toList());

    }
    private WorkReviewDto convertTOWorkReviewDto(WorkReviews reviews){
        return new WorkReviewDto(reviews);
    }
}
