package com.cleansolution.illkam.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailAndPassword(String email, String password);
    Optional<Users> findByBusinessNumber(String email);

    // New Feature 2: Admin login and logout
    // Add this method for admin check
    Optional<Users> findByEmailAndPasswordAndIsAdminTrue(String email, String password);

    // New Feature 1: Admin Posts and Notifications
    List<Users> findByMarketingConsentTrue();

}
