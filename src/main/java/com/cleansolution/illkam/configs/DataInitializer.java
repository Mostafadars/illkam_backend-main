package com.cleansolution.illkam.configs;

import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetailsRepository;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypesRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

import java.util.*;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeWorkTypes(WorkTypesRepository workTypesRepository, WorkTypeDetailsRepository workTypeDetailsRepository) {
        return args -> {
            // 배열로 초기 데이터를 준비
            List<WorkTypes> workTypeLists = new ArrayList<WorkTypes>();
            List<WorkTypeDetails> workTypeDetailLists = new ArrayList<WorkTypeDetails>();

            // WorkTypeDim 테이블이 비어 있으면 초기 데이터를 삽입
            if (workTypesRepository.count() == 0) {
                for(Map<String, Object> workType: WorkTypeBaseFile.WORK_TYPES){

                    WorkTypes workTypes = WorkTypes.builder()
                            .name((String) workType.get("name"))
                            .workInfoDetail((List<Map<String, Object>>) workType.get("workInfo"))
                            .build();
                    System.out.println(workType.get("workInfo"));
                    workTypeLists.add(workTypes);
                    List<String> workTypeDetailsRaw = (List<String>) workType.get("details");
                    for(String detail : workTypeDetailsRaw){
                        WorkTypeDetails workTypeDetails =WorkTypeDetails.builder()
                                .workTypes(workTypes)
                                .name(detail)
                                .build();
                        workTypeDetailLists.add(workTypeDetails);
                    }
                }
                workTypesRepository.saveAll(workTypeLists);
                workTypeDetailsRepository.saveAll(workTypeDetailLists);
                System.out.println("WorkTypeDim 테이블에 초기 데이터를 삽입했습니다.");
            } else {
                System.out.println("WorkTypeDim 테이블에 이미 데이터가 존재합니다.");
            }
        };
    }

}