package com.cleansolution.illkam.version;

import com.cleansolution.illkam.version.dto.VersionCheckRequest;
import com.cleansolution.illkam.version.dto.VersionCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppVersionService {

    private final MobileAppVersionRepository versionRepository;

    public VersionCheckResponse checkForUpdate(VersionCheckRequest request) {
        Optional<MobileAppVersion> latestVersionOpt = versionRepository.findLatestVersion();

        if (latestVersionOpt.isEmpty()) {
            return createNoUpdateResponse();
        }

        MobileAppVersion latestVersion = latestVersionOpt.get();
        String currentVersion = request.getCurrentVersion();

        VersionCheckResponse response = new VersionCheckResponse();
        response.setUpdateAvailable(!latestVersion.getVersionCode().equals(currentVersion));

        return response;
    }

    private VersionCheckResponse createNoUpdateResponse() {
        VersionCheckResponse response = new VersionCheckResponse();
        response.setUpdateAvailable(false);
        return response;
    }
}