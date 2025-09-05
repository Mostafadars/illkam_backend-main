package com.cleansolution.illkam.users.dto;

import lombok.Data;
import lombok.Builder;

// New Feature 2: Admin login and logout

@Data
@Builder // Add this annotation

public class AdminLoginResponseDto {
    private Long id;
    private String name;
    private String email;
    private Boolean isAdmin = true;
}