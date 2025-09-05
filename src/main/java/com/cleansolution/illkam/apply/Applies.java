// package com.cleansolution.illkam.apply;

// import com.cleansolution.illkam.base.BaseTimestampEntity;
// import com.cleansolution.illkam.users.Users;
// import com.cleansolution.illkam.works.Works;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import jakarta.persistence.*;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.experimental.SuperBuilder;

// @SuperBuilder
// @Entity
// @Getter
// @NoArgsConstructor
// public class Applies extends BaseTimestampEntity {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private Integer peopleCount;

//     @ManyToOne
//     @JoinColumn(name = "applier_id")
//     private Users applier;

//     @ManyToOne
//     @JoinColumn(name = "works_id")
//     @JsonIgnore
//     private Works works;

//     private Integer status;

//     @Builder
//     public Applies(Integer peopleCount, Users applier, Works works, Integer status) {
//         this.peopleCount = peopleCount;
//         this.applier = applier;
//         this.works = works;
//         this.status = 0;
//     }

//     public void update(Integer status) {
//         this.status = status;
//     }
// }
package com.cleansolution.illkam.apply;

import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder // Use SuperBuilder for the entire class, including inherited fields
@Entity
@Getter
@NoArgsConstructor
public class Applies extends BaseTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer peopleCount;

    @ManyToOne
    @JoinColumn(name = "applier_id")
    private Users applier;

    @ManyToOne
    @JoinColumn(name = "works_id")
    @JsonIgnore
    private Works works;

    // Use @Builder.Default to set a default value when using the builder
    @Builder.Default
    private Integer status = 0;

    // This constructor is no longer needed, as @SuperBuilder will generate one.
    // If you need it for other purposes (like JPA), you can create an
    // @AllArgsConstructor.
    // However, @SuperBuilder and @NoArgsConstructor are usually sufficient.

    public void update(Integer status) {
        this.status = status;
    }
}