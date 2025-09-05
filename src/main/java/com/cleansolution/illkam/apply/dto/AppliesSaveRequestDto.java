package com.cleansolution.illkam.apply.dto;


import com.cleansolution.illkam.apply.Applies;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppliesSaveRequestDto {
    @JsonProperty("people_count")
    private Integer peopleCount;
    private Long applier_id;
    private Long work_id;
    private Users applier;
    private Works works;

    @Builder
    public AppliesSaveRequestDto(Integer peopleCount,
                                 Long applier_id,
                                 Long work_id
    ) {
        this.applier_id = applier_id;
        this.work_id = work_id;
        this.peopleCount = peopleCount;
    }

    public void updateUsersAndWorks(Users applier, Works works) {
        this.applier = applier;
        this.works = works;
    }

    public Applies toEntity() {
        return Applies.builder()
                .applier(applier)
                .works(works)
                .status(0)
                .build();
    }

}
