package com.cleansolution.illkam.users.dto;

import lombok.Data;

// New Feature 2: Admin login and logout

@Data
public class AdminLoginDto {
    private String email;
    private String password;
}