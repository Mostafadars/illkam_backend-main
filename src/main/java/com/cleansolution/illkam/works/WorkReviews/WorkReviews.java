package com.cleansolution.illkam.works.WorkReviews;

import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.dto.WorkReviewSaveDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class WorkReviews extends BaseTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "work_id")
    private Works work;

    private String contents;

    private String image;

    private Integer starCount;

    @ManyToOne
    @JoinColumn(name = "writer")
    private Users writer;

    @ManyToOne
    @JoinColumn(name = "target")
    private Users target;

    public void update(String contents, Integer starCount, String image){
        this.contents = contents;
        this.starCount =starCount;
        this.image =image;
    }
}
