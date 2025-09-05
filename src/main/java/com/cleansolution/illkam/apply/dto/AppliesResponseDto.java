package com.cleansolution.illkam.apply.dto;

import com.cleansolution.illkam.apply.Applies;
import com.cleansolution.illkam.base.BaseDto;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppliesResponseDto extends BaseDto {
    private Long id;
    private Integer peopleCount;
    private Users applier;
    private Works works;

    public AppliesResponseDto(Applies entity) {
        this.id = entity.getId();
        this.peopleCount = entity.getPeopleCount();
        this.applier = entity.getApplier();
        this.works = entity.getWorks();
        super.setCreatedDateTime(entity.getCreatedDateTime());
        super.setModifiedDateTime(entity.getModifiedDateTime());
    }
}
