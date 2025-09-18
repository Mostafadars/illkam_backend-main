package com.cleansolution.illkam.version.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VersionCheckRequest {
    private String platform; // "ios" or "android"
    private String currentVersion;
    private String appId; // Optional: if you have multiple apps

}
