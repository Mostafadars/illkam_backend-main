package com.cleansolution.illkam.works.WorkReviews;

import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkReviewsRepository extends JpaRepository<WorkReviews, Long> {

    Optional<WorkReviews> findByWorkAndWriter(Works works, Users writer);

    List<WorkReviews> findAllByTarget(Users users);
}
