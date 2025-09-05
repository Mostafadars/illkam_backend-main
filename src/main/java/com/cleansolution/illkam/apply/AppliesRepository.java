package com.cleansolution.illkam.apply;

import com.cleansolution.illkam.works.Works;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppliesRepository extends JpaRepository<Applies, Long> {

    List<Applies> findByWorks(Works works);
}

