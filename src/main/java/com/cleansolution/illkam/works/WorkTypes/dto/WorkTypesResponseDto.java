package com.cleansolution.illkam.works.WorkTypes.dto;

import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class WorkTypesResponseDto {
    private Long id;
    private String name;
    private List<Map<String, Object>> workInfoDetail;
    private List<WorkTypeDetails> workTypeDetails;

    public WorkTypesResponseDto(WorkTypes entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.workInfoDetail =entity.getWorkInfoDetail();
        this.workTypeDetails = entity.getWorkTypeDetailsList();
    }
}
