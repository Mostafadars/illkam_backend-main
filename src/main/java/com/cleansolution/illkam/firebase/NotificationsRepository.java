package com.cleansolution.illkam.firebase;

import com.cleansolution.illkam.firebase.entity.Notifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications,Long> {
    List<Notifications> findByUserId(Long id, Sort sort);
}
