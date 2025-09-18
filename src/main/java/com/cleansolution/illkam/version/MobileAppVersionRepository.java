package com.cleansolution.illkam.version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MobileAppVersionRepository extends JpaRepository<MobileAppVersion, Long> {

    @Query("SELECT m FROM MobileAppVersion m ORDER BY m.createdAt DESC LIMIT 1")
    Optional<MobileAppVersion> findLatestVersion();

    Optional<MobileAppVersion> findByVersionCode(String versionCode);
}