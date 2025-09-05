package com.cleansolution.illkam.works.WorkTypes.detail;

import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class WorkTypeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @ManyToOne
    @JoinColumn(name = "work_types_id")
    @JsonIgnore
    private WorkTypes workTypes;

    @OneToMany(mappedBy = "workTypeDetails", cascade = CascadeType.REMOVE,fetch = FetchType.EAGER )
    @JsonIgnore
    private List<Works> works;

    @Builder
    public WorkTypeDetails(String name, WorkTypes workTypes){
        this.name = name;
        this.workTypes = workTypes;
    }

    public void updateName(String name){
        this.name = name;
    }
}
