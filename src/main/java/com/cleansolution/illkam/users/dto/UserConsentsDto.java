package com.cleansolution.illkam.users.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserConsentsDto {
    private Boolean marketingConsent;
    private Boolean orderConsent;
}