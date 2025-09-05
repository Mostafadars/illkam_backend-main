package com.cleansolution.illkam.works.dto;

import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.images.WorkImages;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class WorksSaveRequestDto {
    @JsonProperty("workDate")
    private LocalDate workDate;

    @JsonProperty("workHour")
    private String workHour;

    @JsonProperty("workLocationSi")
    private String workLocationSi;
    @JsonProperty("workLocationGu")
    private String workLocationGu;
    @JsonProperty("workLocationDong")
    private String workLocationDong;

    @JsonProperty("workMainType")
    private String workMainType;
    @JsonProperty("workDetailType")
    private String workDetailType;

    @JsonProperty("workInfoDetail")
    private List<Map<String, Object>> workInfoDetail;

    private Integer price;

    @Column(length = 500)
    private String comments;

    private Long employer_id;


    @JsonProperty("workImages")
    private List<String> workImages;

    @Builder
    WorksSaveRequestDto(
            LocalDate workDate,
            String workHour,
            String workLocationSi,
            String workLocationGu,
            String workLocationDong,
            String workMainType,
            String workDetailType,
            List<Map<String, Object>> workInfoDetail,
            List<String> workImages,
            Integer price,
            String comments
    ) {
        this.workDate = workDate;
        this.workHour = workHour;
        this.workLocationSi = workLocationSi;
        this.workLocationGu = workLocationGu;
        this.workLocationDong = workLocationDong;
        this.workMainType = workMainType;
        this.workDetailType = workDetailType;
        this.workInfoDetail = workInfoDetail;
        this.price = price;
        this.comments = comments;
        this.workImages = workImages;
    }

    public Works toEntity(Users employer, WorkTypes workTypes, WorkTypeDetails workTypeDetails) {
        return Works.builder()
                .workDate(workDate)
                .workHour(workHour)
                .workLocationSi(workLocationSi)
                .workLocationGu(workLocationGu)
                .workLocationDong(workLocationDong)
                .workTypes(workTypes)
                .workTypeDetails(workTypeDetails)
                .workInfoDetail(workInfoDetail)
                .price(price)
                .comments(comments)
                .employer(employer)
                .isConfirmed(false)
                .build();
    }

}
