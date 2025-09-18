package com.cleansolution.illkam.version;

import com.cleansolution.illkam.version.dto.VersionCheckRequest;
import com.cleansolution.illkam.version.dto.VersionCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppVersionService {

    private final MobileAppVersionRepository versionRepository;

    public VersionCheckResponse checkForUpdate(VersionCheckRequest request) {
        List<MobileAppVersion> latestVersions = versionRepository
                .findLatestByPlatform(request.getPlatform().toLowerCase());

        if (latestVersions.isEmpty()) {
            return createNoUpdateResponse();
        }

        MobileAppVersion latestVersion = latestVersions.get(0);
        int currentVersionNumber = parseVersionNumber(request.getCurrentVersion());

        VersionCheckResponse response = new VersionCheckResponse();

        if (latestVersion.getVersionNumber() > currentVersionNumber) {
            response.setUpdateAvailable(true);
            response.setForceUpdate(latestVersion.getForceUpdate());
            response.setLatestVersion(latestVersion.getVersionCode());
            response.setMinRequiredVersion(latestVersion.getMinRequiredVersion());
            response.setStoreUrl(latestVersion.getStoreUrl());
            response.setReleaseNotes(latestVersion.getReleaseNotes());

            // Check if force update is required based on min required version
            if (latestVersion.getMinRequiredVersion() != null) {
                int minRequiredVersion = parseVersionNumber(latestVersion.getMinRequiredVersion());
                if (currentVersionNumber < minRequiredVersion) {
                    response.setForceUpdate(true);
                }
            }
        } else {
            response.setUpdateAvailable(false);
        }

        return response;
    }

    int parseVersionNumber(String version) {
        // Convert version string like "1.2.3" to number 10203
        String[] parts = version.split("\\.");
        int major = parts.length > 0 ? Integer.parseInt(parts[0]) : 0;
        int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        return major * 10000 + minor * 100 + patch;
    }

    private VersionCheckResponse createNoUpdateResponse() {
        VersionCheckResponse response = new VersionCheckResponse();
        response.setUpdateAvailable(false);
        response.setForceUpdate(false);
        return response;
    }
}