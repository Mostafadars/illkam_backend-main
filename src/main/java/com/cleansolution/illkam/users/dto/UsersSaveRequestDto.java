package com.cleansolution.illkam.users.dto;

import com.cleansolution.illkam.users.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UsersSaveRequestDto {
    @JsonProperty("name")
    private String name;
    private String password;

    @JsonProperty("businessAddress")
    private String businessAddress;

    @JsonProperty("businessNumber")
    private String businessNumber;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    private String email;

    @JsonProperty("fcmToken")
    private String fcmToken;

    @JsonProperty("businessCertification")
    private String businessCertification;

    @Builder
    public UsersSaveRequestDto(String name,
                               String businessAddress,
                               String businessNumber,
                               String phoneNumber,
                               String email,
                               String password,
                               String businessCertification

    ) {
        this.name = name;
        this.businessNumber = businessAddress;
        this.businessAddress = businessNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.businessCertification = businessCertification;
    }

    public Users toEntity() {
        return Users.builder()
                .name(name)
                .businessNumber(businessNumber) // may have bug here with the business address
                .businessAddress(businessAddress)
                .phoneNumber(phoneNumber)
                .password(password)
                .email(email)
                .businessCertification(businessCertification)
                .build();
    }
}
