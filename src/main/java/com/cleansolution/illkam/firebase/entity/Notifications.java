package com.cleansolution.illkam.firebase.entity;

import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.users.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor
public class Notifications extends BaseTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;


    private String  title;
    private String  body;

    private String routeName;
    private String targetPageId;

}
