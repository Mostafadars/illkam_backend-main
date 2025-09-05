package com.cleansolution.illkam.works;

import com.cleansolution.illkam.posts.Posts;
import com.cleansolution.illkam.posts.categories.PostsCategories;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.dto.WorksResponseDto;
import com.cleansolution.illkam.works.dto.WorksSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface WorksRepository extends JpaRepository<Works, Long> {
    List<Works> findByWorkDate(LocalDate date);

    @Query("SELECT w FROM Works w WHERE w.workDate BETWEEN :startDate AND :endDate")
    List<Works> findWorksBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.cleansolution.illkam.works.dto.WorksResponseDto(w) FROM Works w WHERE (:workLocationSi IS NULL OR w.workLocationSi = :workLocationSi) " +
            "AND (:workLocationGu IS NULL OR w.workLocationGu = :workLocationGu) " +
            "AND (:workLocationDong IS NULL OR w.workLocationDong = :workLocationDong) " +
            "AND (:workLocationGu IS NULL OR w.workLocationGu = :workLocationGu) " +
            "AND (:workTypeDetailId IS NULL OR w.workTypes.id = :workTypeDetailId) " +
            "AND (:workTypeId IS NULL OR w.workTypeDetails.id = :workTypeId) " +
            "AND (:workDate IS NULL OR w.workDate = :workDate) " +
            "AND w.workDate >= CURRENT_DATE")
    Page<WorksResponseDto> findAllByFilters(@Param("workLocationSi") String workLocationSi,
                                            @Param("workLocationGu") String workLocationGu,
                                            @Param("workLocationDong") String workLocationDong,
                                            @Param("workTypeDetailId") Long workTypeDetailId,
                                            @Param("workTypeId") Long workTypeId,
                                            @Param("workDate") LocalDate workDate,
                                            Pageable pageable
                                   );

    @Query("SELECT new com.cleansolution.illkam.works.dto.WorksResponseDto(w) FROM Works w WHERE (:workLocationSi IS NULL OR w.workLocationSi = :workLocationSi) " +
            "AND (:workLocationGu IS NULL OR w.workLocationGu = :workLocationGu) " +
            "AND (:workLocationDong IS NULL OR w.workLocationDong = :workLocationDong) " +
            "AND (:workLocationGu IS NULL OR w.workLocationGu = :workLocationGu) " +
            "AND (:workTypeDetailId IS NULL OR w.workTypes.id = :workTypeDetailId) " +
            "AND (:workTypeId IS NULL OR w.workTypeDetails.id = :workTypeId) " +
            "AND (:workDate IS NULL OR w.workDate = :workDate)")
    Page<WorksResponseDto> findAllByAdmin(@Param("workLocationSi") String workLocationSi,
                                            @Param("workLocationGu") String workLocationGu,
                                            @Param("workLocationDong") String workLocationDong,
                                            @Param("workTypeDetailId") Long workTypeDetailId,
                                            @Param("workTypeId") Long workTypeId,
                                            @Param("workDate") LocalDate workDate,
                                            Pageable pageable
    );

    @Query("SELECT new com.cleansolution.illkam.works.dto.WorksSummaryResponseDto(w.workDate, COUNT(w)) " +
            "FROM Works w " +
            "WHERE (:workLocationSi IS NULL OR w.workLocationSi = :workLocationSi) " +
            "AND (:workLocationGu IS NULL OR w.workLocationGu = :workLocationGu) " +
            "AND (:workLocationDong IS NULL OR w.workLocationDong = :workLocationDong) " +
            "AND (:workTypeDetailId IS NULL OR w.workTypeDetails.id = :workTypeDetailId) " +
            "AND (:workTypeId IS NULL OR w.workTypes.id = :workTypeId) " +
            "AND w.workDate BETWEEN :startDate AND :endDate " +
            "GROUP BY w.workDate")
    List<WorksSummaryResponseDto> findWorkSummary(
            @Param("workLocationSi") String workLocationSi,
            @Param("workLocationGu") String workLocationGu,
            @Param("workLocationDong") String workLocationDong,
            @Param("workTypeDetailId") Long workTypeDetailId,
            @Param("workTypeId") Long workTypeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    List<Works> findByEmployerIdOrderByWorkDateDesc(Long userid, Sort sort);
    // 특정 Applier의 userId가 포함된 Apply가 있는 Work 리스트를 가져오는 JPQL

    // WorkDate를 내림차순으로 정렬하여 전체 Works 리스트를 가져오는 JPQL
    @Query("SELECT w FROM Works w JOIN w.appliesList a WHERE a.applier.id = :userId AND a.status != 9 ORDER BY w.workDate DESC")
    List<Works> findWorksByApplierIdWorkDateDesc(@Param("userId") Long userId, Sort sort);

}
