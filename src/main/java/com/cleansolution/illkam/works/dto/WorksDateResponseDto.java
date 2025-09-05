package com.cleansolution.illkam.works.dto;

import com.cleansolution.illkam.base.BaseDto;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.Works;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class WorksDateResponseDto {
    private LocalDate workDate;
    @Builder
    WorksDateResponseDto(
            LocalDate workDate
    ) {
        this.workDate = workDate;
    }
}