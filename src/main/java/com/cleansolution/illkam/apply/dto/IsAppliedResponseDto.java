package com.cleansolution.illkam.apply.dto;

// New Feature 12: Information exposure restriction and chatroom entry filtering

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IsAppliedResponseDto {
    private Boolean isApplied;
    private Integer applyStatus; // 0 = applied, 1 = confirmed, 9 = cancelled

    public IsAppliedResponseDto(Boolean isApplied, Integer applyStatus) {
        this.isApplied = isApplied;
        this.applyStatus = applyStatus;
    }
}