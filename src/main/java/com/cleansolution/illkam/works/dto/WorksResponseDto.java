package com.cleansolution.illkam.works.dto;

import com.cleansolution.illkam.apply.Applies;
import com.cleansolution.illkam.apply.dto.AppliesResponseDto;
import com.cleansolution.illkam.base.BaseDto;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.images.WorkImages;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class WorksResponseDto extends BaseDto {
    private Long id;
    private LocalDate workDate;
    private String workHour;
    private WorkTypes workTypes;
    private WorkTypeDetails workTypeDetails;
    private String workLocationSi;
    private String workLocationGu;
    private String workLocationDong;
    private List<Map<String, Object>> workInfoDetail;
    private List<String> workImages;
    private Integer price;
    private String comments;
    private Users employer;
    private Users employee;
    private Boolean isConfirmed;
    private Boolean isReviewed;
    private List<Applies> appliesList;
    private List<WorkReviews> workReviews;
    private Boolean didEmployeeReviewed;

    public WorksResponseDto(Works entity){
        this.id= entity.getId();
        this.workHour= entity.getWorkHour();
        this.workDate =entity.getWorkDate();
        this.workTypeDetails = entity.getWorkTypeDetails();
        this.workLocationGu =entity.getWorkLocationGu();
        this.workLocationDong =entity.getWorkLocationDong();
        this.workLocationSi =entity.getWorkLocationSi();
        this.workTypes = entity.getWorkTypes();
        this.workInfoDetail = entity.getWorkInfoDetail();
        this.price = entity.getPrice();
        this.comments = entity.getComments();
        this.employer = entity.getEmployer();
        this.employee = entity.getEmployee();
        this.appliesList = entity.getAppliesList();
        this.workReviews = entity.getReviews();
        this.isConfirmed = entity.getIsConfirmed();
        this.isReviewed = entity.getIsReviewed();
        this.workImages = entity.getWorkImages().stream().map(this::convertToDTO).collect(Collectors.toList());
        super.setCreatedDateTime(entity.getCreatedDateTime());
        super.setModifiedDateTime(entity.getModifiedDateTime());
    }

    public WorksResponseDto(Works entity, Boolean didEmployeeReviewed){
        this.id= entity.getId();
        this.workHour= entity.getWorkHour();
        this.workDate =entity.getWorkDate();
        this.workTypeDetails = entity.getWorkTypeDetails();
        this.workLocationGu =entity.getWorkLocationGu();
        this.workLocationDong =entity.getWorkLocationDong();
        this.workLocationSi =entity.getWorkLocationSi();
        this.workTypes = entity.getWorkTypes();
        this.workInfoDetail = entity.getWorkInfoDetail();
        this.price = entity.getPrice();
        this.comments = entity.getComments();
        this.employer = entity.getEmployer();
        this.employee = entity.getEmployee();
        this.appliesList = entity.getAppliesList();
        this.workReviews = entity.getReviews();
        this.isConfirmed = entity.getIsConfirmed();
        this.isReviewed = entity.getIsReviewed();
        this.workImages = entity.getWorkImages().stream().map(this::convertToDTO).collect(Collectors.toList());
        this.didEmployeeReviewed = didEmployeeReviewed;
        super.setCreatedDateTime(entity.getCreatedDateTime());
        super.setModifiedDateTime(entity.getModifiedDateTime());
    }

    private String convertToDTO(WorkImages workImages) {
        return workImages.getPhotoURL();
    }
}
