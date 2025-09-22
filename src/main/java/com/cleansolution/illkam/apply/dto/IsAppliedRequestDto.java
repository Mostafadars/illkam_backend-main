package com.cleansolution.illkam.apply.dto;

// New Feature 12: Information exposure restriction and chatroom entry filtering

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IsAppliedRequestDto {
    private Long applierId;
    private Long workId;
}