package com.cleansolution.illkam.users.UserTypes;

import com.cleansolution.illkam.users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class CompanyUsers {
    @Id
    private Long userId;

    @MapsId(value = "userId")
    @OneToOne(targetEntity = Users.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(length = 20)
    private String businessNumber;

    @Column(length = 50)
    private String email;

    @Column
    private String businessAddress;

    @Column
    private String businessCertification;

    @Column
    private Boolean isApproved;

    @Column
    private LocalDateTime approveDate;

}
