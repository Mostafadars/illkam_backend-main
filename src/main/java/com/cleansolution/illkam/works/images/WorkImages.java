package com.cleansolution.illkam.works.images;

import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@Getter
@SuperBuilder
public class WorkImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoURL;

    @ManyToOne
    @JoinColumn(name = "work_id")
    @JsonIgnore
    private Works work;


}
