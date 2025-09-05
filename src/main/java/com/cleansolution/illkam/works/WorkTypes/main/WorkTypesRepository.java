package com.cleansolution.illkam.works.WorkTypes.main;

import org.springframework.data.jpa.repository.JpaRepository;


public interface WorkTypesRepository  extends JpaRepository<WorkTypes, Long> {
    WorkTypes findByName(String name);
}
