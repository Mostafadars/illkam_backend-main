package com.cleansolution.illkam.works.WorkTypes.dto;

import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class WorkTypeDetailsSaveRequestDto {

    private String name;

    @JsonProperty("workTypesId")
    private Long workTypesId;

    private WorkTypes workTypes;


    @Builder
    public WorkTypeDetailsSaveRequestDto(String name, Long workTypesId) {
        this.name = name;
        this.workTypesId = workTypesId;
    }

    public void updateWorkType(WorkTypes workTypes) {
        this.workTypes = workTypes;
    }

    public WorkTypeDetails toEntity() {
        return WorkTypeDetails.builder()
                .name(name)
                .workTypes(workTypes)
                .build();
    }

}
