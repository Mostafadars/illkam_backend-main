package com.cleansolution.illkam.version;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MobileAppVersionRepository extends JpaRepository<MobileAppVersion, Long> {

    @Query("SELECT m FROM MobileAppVersion m WHERE m.platform = :platform " +
            "AND m.isActive = true ORDER BY m.versionNumber DESC")
    List<MobileAppVersion> findLatestByPlatform(@Param("platform") String platform);

    Optional<MobileAppVersion> findByPlatformAndVersionCode(String platform, String versionCode);
}
