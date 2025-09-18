package com.cleansolution.illkam.version.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VersionCheckResponse {
    private boolean updateAvailable;
}