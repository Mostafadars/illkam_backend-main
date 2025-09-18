package com.cleansolution.illkam.version;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "mobile_app_version")
public class MobileAppVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String platform; // "ios" or "android"

    @Column(nullable = false)
    private String versionCode;

    @Column(nullable = false)
    private Integer versionNumber;

    private String minRequiredVersion;
    private Boolean forceUpdate = false;
    private String releaseNotes;
    private String storeUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Boolean isActive = true;

    public MobileAppVersion(String platform, String versionCode, Integer versionNumber, String minRequiredVersion, Boolean forceUpdate, String releaseNotes, String storeUrl, Boolean isActive) {
        this.platform = platform;
        this.versionCode = versionCode;
        this.versionNumber = versionNumber;
        this.minRequiredVersion = minRequiredVersion;
        this.forceUpdate = forceUpdate;
        this.releaseNotes = releaseNotes;
        this.storeUrl = storeUrl;
        this.isActive = isActive;
    }

}