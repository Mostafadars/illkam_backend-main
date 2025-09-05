package com.cleansolution.illkam.users.UserTypes;

import com.cleansolution.illkam.users.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomerUsers {
    @Id
    private Long userId;

    @MapsId(value = "userId")
    @OneToOne(targetEntity = Users.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column
    private Boolean isNative;

    @Column
    private String location;

    @Column
    private Integer age;

    @Column
    private String profileURL;

}
