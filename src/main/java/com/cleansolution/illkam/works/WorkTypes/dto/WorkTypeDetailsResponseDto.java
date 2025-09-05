package com.cleansolution.illkam.works.WorkTypes.dto;


import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkTypeDetailsResponseDto {
    private Long id;
    private String name;
    private WorkTypes workTypes;

    public WorkTypeDetailsResponseDto(WorkTypeDetails entity){
        this.id = entity.getId();
        this.name =entity.getName();
        this.workTypes = entity.getWorkTypes();
    }
}
