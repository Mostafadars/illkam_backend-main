package com.cleansolution.illkam.version;

import com.cleansolution.illkam.version.dto.VersionCheckRequest;
import com.cleansolution.illkam.version.dto.VersionCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class AppVersionController {

    private final AppVersionService appVersionService;
    private final MobileAppVersionRepository versionRepository;

    @PostMapping("/version/check")
    public ResponseEntity<VersionCheckResponse> checkVersion(
            @RequestBody VersionCheckRequest request) {

        // Validate request
        if (request.getPlatform() == null || request.getCurrentVersion() == null) {
            throw new IllegalArgumentException("Platform and currentVersion are required");
        }

        VersionCheckResponse response = appVersionService.checkForUpdate(request);
        return ResponseEntity.ok(response);
    }

    // Admin endpoint to manage versions
    @PostMapping("/version")
    public ResponseEntity<MobileAppVersion> createVersion(
            @RequestBody MobileAppVersion version) {
        // Calculate version number
        version.setVersionNumber(appVersionService.parseVersionNumber(version.getVersionCode()));
        MobileAppVersion savedVersion = versionRepository.save(version);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVersion);
    }
}