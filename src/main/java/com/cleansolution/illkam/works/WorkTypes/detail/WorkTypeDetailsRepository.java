package com.cleansolution.illkam.works.WorkTypes.detail;

import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTypeDetailsRepository extends JpaRepository<WorkTypeDetails, Long> {
    WorkTypeDetails findByName(String name);
    WorkTypeDetails findByNameAndWorkTypes(String name, WorkTypes workTypes);
}
