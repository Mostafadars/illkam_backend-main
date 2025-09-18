package com.cleansolution.illkam.version.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VersionCheckResponse {
    private boolean updateAvailable;
    private boolean forceUpdate;
    private String latestVersion;
    private String minRequiredVersion;
    private String storeUrl;
    private String releaseNotes;

}