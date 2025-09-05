package com.cleansolution.illkam.apply.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppliesUpdateDto {
    private Integer status;

    @Builder
    public AppliesUpdateDto(Integer status) {
        this.status = status;
    }
}
