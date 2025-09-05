// package com.cleansolution.illkam.works.WorkTypes.main;

// import com.cleansolution.illkam.base.JsonConverter;
// import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import jakarta.persistence.*;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.experimental.SuperBuilder;

// import java.util.List;
// import java.util.Map;

// @Entity
// @Getter
// @NoArgsConstructor
// @SuperBuilder
// public class WorkTypes {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String name;

//     @OneToMany(mappedBy = "workTypes", cascade = CascadeType.REMOVE,fetch = FetchType.EAGER )
//     @JsonIgnore
//     private List<WorkTypeDetails> workTypeDetailsList;

//     @Column(length = 5000)
//     @Convert(converter = JsonConverter.class)
//     private List<Map<String, Object>> workInfoDetail;

//     @Builder
//     public WorkTypes(String name, List<Map<String, Object>> workInfoDetail){
//         this.name = name;
//         this.workInfoDetail =workInfoDetail;
//     }

//     public void updateInfo (List<Map<String, Object>> info){
//         this.workInfoDetail = info;
//     }

//     public void updateName(String name){
//         this.name = name;
//     }

// }

package com.cleansolution.illkam.works.WorkTypes.main;

import com.cleansolution.illkam.base.JsonConverter;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder // This will generate the builder for all fields in the class.
public class WorkTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "workTypes", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<WorkTypeDetails> workTypeDetailsList;

    @Column(length = 5000)
    @Convert(converter = JsonConverter.class)
    private List<Map<String, Object>> workInfoDetail;

    // The constructor with @Builder has been removed.
    // @SuperBuilder will generate the necessary logic.

    public void updateInfo(List<Map<String, Object>> info) {
        this.workInfoDetail = info;
    }

    public void updateName(String name) {
        this.name = name;
    }
}