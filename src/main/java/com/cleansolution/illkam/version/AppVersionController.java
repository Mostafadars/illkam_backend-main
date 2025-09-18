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
        if (request.getCurrentVersion() == null) {
            throw new IllegalArgumentException("currentVersion is required");
        }

        VersionCheckResponse response = appVersionService.checkForUpdate(request);
        return ResponseEntity.ok(response);
    }

    // Admin endpoint to create new version
    @PostMapping("/version")
    public ResponseEntity<MobileAppVersion> createVersion(
            @RequestBody MobileAppVersion version) {
        MobileAppVersion savedVersion = versionRepository.save(version);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVersion);
    }
}