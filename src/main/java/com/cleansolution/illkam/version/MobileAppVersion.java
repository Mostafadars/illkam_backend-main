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
    private String versionCode;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public MobileAppVersion(String versionCode) {
        this.versionCode = versionCode;
    }
}