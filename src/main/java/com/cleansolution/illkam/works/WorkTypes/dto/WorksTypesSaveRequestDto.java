package com.cleansolution.illkam.works.WorkTypes.dto;

import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class WorksTypesSaveRequestDto {
    private Long id;

    private String name;

    private List<Map<String, Object>> workInfoDetail;


    @Builder
    public WorksTypesSaveRequestDto(String name, List<Map<String, Object>> workInfoDetail) {
        this.workInfoDetail =workInfoDetail;
        this.name = name;
    }

    public WorkTypes toEntity() {
        return WorkTypes.builder()
                .name(name)
                .workInfoDetail(workInfoDetail)
                .build();
    }
}
