package com.cleansolution.illkam.works;


import com.cleansolution.illkam.apply.Applies;
import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.base.JsonConverter;
import com.cleansolution.illkam.chats.Chats;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.cleansolution.illkam.works.images.WorkImages;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class Works extends BaseTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate workDate;

    @ManyToOne
    @JoinColumn(name = "work_types_id")
    private WorkTypes workTypes;

    @ManyToOne
    @JoinColumn(name = "work_type_details_id")
    private WorkTypeDetails workTypeDetails;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Users employer;

    @OneToMany(mappedBy = "work", cascade = CascadeType.PERSIST )
    @JsonIgnore
    private List<Chats> chats;

    private String workHour;

    private String workLocationSi;

    private String workLocationGu;

    private String workLocationDong;

    private Boolean isConfirmed;

    private Boolean isReviewed;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Users employee;

    @Column(length = 5000)
    @Convert(converter = JsonConverter.class)
    private List<Map<String, Object>> workInfoDetail;

    private Integer price;

    @ColumnDefault("0")
    private Integer peopleCount;

    private String comments;

    @OneToMany(mappedBy = "work", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<WorkImages> workImages = new ArrayList<>();

    @OneToMany(mappedBy = "works", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Applies> appliesList = new ArrayList<>();

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WorkReviews> reviews;

//    TODO 운영 방침 물어봐야 함
    public void update(
            LocalDate workDate,
                       String workHour,

                      Integer price) {
        this.workDate = workDate;
        this.workHour = workHour;
        this.price = price;
    }

    public void updateWorkStatus(Boolean value, Users user) {
        this.employee = user;
        this.isConfirmed = value;
    }

    public void updateWorkAsReviewed(){
        this.isReviewed = true;
    }

    @PreRemove
    public void preRemove() {
        for (Chats child : chats) {
            child.setWork(null);
        }
    }

}
