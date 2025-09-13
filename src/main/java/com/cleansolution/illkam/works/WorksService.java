package com.cleansolution.illkam.works;


import com.cleansolution.illkam.apply.Applies;
import com.cleansolution.illkam.apply.AppliesRepository;
import com.cleansolution.illkam.firebase.FCMService;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkReviews.WorkReviewsRepository;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetailsRepository;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypesRepository;
import com.cleansolution.illkam.works.dto.WorkReviewSaveDto;
import com.cleansolution.illkam.works.dto.WorksResponseDto;
import com.cleansolution.illkam.works.dto.WorksSaveRequestDto;
import com.cleansolution.illkam.works.dto.WorksSummaryResponseDto;
import com.cleansolution.illkam.works.images.WorkImages;
import com.cleansolution.illkam.works.images.WorkImagesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorksService {
    private final WorksRepository worksRepository;
    private final WorkTypesRepository workTypesRepository;
    private final WorkTypeDetailsRepository workTypeDetailsRepository;
    private final UsersRepository usersRepository;
    private final WorkReviewsRepository workReviewsRepository;
    private final WorkImagesRepository workImagesRepository;
    private final FCMService fcmService;
    private final AppliesRepository appliesRepository;


    @Transactional
    public Long save(WorksSaveRequestDto dto) {
        WorkTypes workTypes = workTypesRepository.findByName(dto.getWorkMainType());
        WorkTypeDetails workTypeDetails = workTypeDetailsRepository.findByNameAndWorkTypes(dto.getWorkDetailType(), workTypes);
        Users users = usersRepository.findById(dto.getEmployer_id()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + dto.getEmployer_id()));
        Works works = worksRepository.save(dto.toEntity(users, workTypes, workTypeDetails));

        List<WorkImages> workImages = new ArrayList<>();
        for (String imageURL : dto.getWorkImages()) {
            WorkImages workImage = WorkImages.builder()
                    .photoURL(imageURL)
                    .work(works)
                    .build();
            workImages.add(workImage);
        }
        workImagesRepository.saveAll(workImages);

        return works.getId();
    }

    @Transactional
    public Long updateById(Long id, WorksSaveRequestDto dto) {
        Works works = worksRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("일깜이 존재하지 않습니다."));
        works.update(dto.getWorkDate(), dto.getWorkHour(), dto.getPrice());
        worksRepository.save(works);
//        일깜 지원한 사람들에게 알림
        works.getAppliesList().forEach((elem) -> {
            try {
                fcmService.sendMessage(
                        elem.getApplier(),
                        "일깜 수정",
                        "지원하신 " + elem.getWorks().getEmployer().getName() + "님의 일깜이 수정되었습니다.",
                        "work",
                        works.getId().toString()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return works.getId();
    }


    public Page<WorksResponseDto> getWorksByFilters(
            String workLocationSi,
            String workLocationGu,
            String workLocationDong,
            Long workTypeDetailId,
            Long workTypeId,
            LocalDate workDate,
            Pageable pageable
    ) {

        return worksRepository.findAllByFilters(workLocationSi, workLocationGu, workLocationDong, workTypeId, workTypeDetailId, workDate, pageable);
    }


    public List<WorksResponseDto> findAll(
            String workLocationSi,
            Long workTypeId,
            Long workTypeDetailId,
            Pageable pageable) {
        return worksRepository.findAllByFilters(workLocationSi, null, null, workTypeId, workTypeDetailId, null, pageable).toList();
    }

    public Page<WorksResponseDto> findAdminAll(
            String workLocationSi,
            Long workTypeId,
            Long workTypeDetailId,
            Pageable pageable) {
        return worksRepository.findAllByAdmin(workLocationSi, null, null, workTypeId, workTypeDetailId, null, pageable);
    }

    @Transactional
    public WorksResponseDto findById(Long id) {

        Works entity = worksRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일깜이 없습니다. id=" + id));
        Optional<WorkReviews> workReviews = workReviewsRepository.findByWorkAndWriter(entity, entity.getEmployer());
        return new WorksResponseDto(entity, workReviews.isPresent());
    }

    @Transactional
    public Boolean deleteById(Long id) {
        Works work = worksRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 일깜이 없습니다."));
        List<Applies> appliesList = work.getAppliesList();
        workImagesRepository.deleteAll(work.getWorkImages());
        appliesList.forEach(apply -> {
            try {
                fcmService.sendMessage(apply.getApplier(), "일깜 삭제 안내", apply.getWorks().getEmployer().getName() + "님의 일깜이 삭제되었습니다.", "work", apply.getWorks().getId().toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            appliesRepository.delete(apply);
        });
        worksRepository.deleteById(id);
        return true;
    }

    //    마이 페이지 용
    public List<WorksResponseDto> findByEmployer(Long userid) {
        Sort sort = Sort.by("workDate").descending();
        return worksRepository.findByEmployerIdOrderByWorkDateDesc(userid, sort)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<WorksResponseDto> findByEmployee(Long userid) {
        Sort sort = Sort.by("workDate").descending();
        return worksRepository.findWorksByApplierIdWorkDateDesc(userid, sort)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<WorksResponseDto> findByDate(LocalDate date) {
        return worksRepository.findByWorkDate(date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<WorksSummaryResponseDto> getWorkSummary(LocalDate date) {
        LocalDate startDate = date.minusDays(2);
        if (startDate.getMonth() == LocalDate.now().getMonth()) {
            startDate = LocalDate.now();
        }
        LocalDate endDate = date.plusDays(5);

        // 해당 범위의 작업 조회
        List<Works> works = worksRepository.findWorksBetweenDates(startDate, endDate);

        // 날짜별로 작업 수를 저장할 맵 생성
        Map<LocalDate, Long> workCountPerDay = new HashMap<>();
        List<Long> workCount = new ArrayList<>();

        // startDate부터 endDate까지 날짜별로 초기화
        for (LocalDate current = startDate; !current.isAfter(endDate); current = current.plusDays(1)) {
            workCountPerDay.put(current, 0L);
        }
        // 각 Work의 날짜에 맞춰서 카운트 증가
        for (Works work : works) {
            LocalDate workDate = work.getWorkDate();
            workCountPerDay.put(workDate, workCountPerDay.get(workDate) + 1);
        }
        // Map을 List<WorkSummaryDTO>로 변환
        List<WorksSummaryResponseDto> workSummaryList = new ArrayList<>();
        for (Map.Entry<LocalDate, Long> entry : workCountPerDay.entrySet()) {
            workSummaryList.add(new WorksSummaryResponseDto(entry.getKey(), entry.getValue()));
        }
        return workSummaryList;
    }

    //    일깜 모집 종료되었는 지 확인
    public Boolean checkWorkApplyValid(Long workId) {
        Optional<Works> works = worksRepository.findById(workId);
        return works.filter(value -> value.getEmployee() != null).isPresent();
    }


    private WorksResponseDto convertToDTO(Works works) {
        return new WorksResponseDto(works);
    }

    public List<WorksSummaryResponseDto> getWorkSummary(
            String workLocationSi,
            String workLocationGu,
            String workLocationDong,
            Long workTypeDetailId,
            Long workTypeId
    ) {

        // 날짜 계산 (3일 전 ~ 6일 후)
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.from(LocalDate.now().plusDays(30).atTime(23, 59, 59));

        // 2. startDate와 endDate 사이의 모든 날짜 리스트를 만듦
        List<LocalDate> allDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        List<WorksSummaryResponseDto> queryResult = worksRepository.findWorkSummary(workLocationSi, workLocationGu, workLocationDong,
                workTypeDetailId, workTypeId, startDate, endDate);

        // 3. 존재하는 데이터 기반으로 HashMap에 날짜별로 workCount를 저장
        Map<LocalDate, Long> workCountMap = queryResult.stream()
                .collect(Collectors.toMap(WorksSummaryResponseDto::getDate, WorksSummaryResponseDto::getWorkCount));

        // 4. 모든 날짜에 대해 없는 날짜는 workCount = 0으로 추가
        List<WorksSummaryResponseDto> result = new ArrayList<>();
        for (LocalDate date : allDates) {
            result.add(new WorksSummaryResponseDto(date, workCountMap.getOrDefault(date, 0L)));
        }


        // 필터를 적용한 데이터 조회
        return result;
    }

    public List<WorksSummaryResponseDto> getWorkSummaryByMonthly(
            String workLocationSi,
            String workLocationGu,
            String workLocationDong,
            Long workTypeDetailId,
            Long workTypeId,
            LocalDate month
    ) {

        // 날짜 계산 (3일 전 ~ 6일 후)
        LocalDate startDate = month.withDayOfMonth(1);
        if (startDate.getMonth() == LocalDate.now().getMonth()) {
            startDate = LocalDate.now();
        }
        LocalDate endDate = month.with(TemporalAdjusters.lastDayOfMonth());

        // 2. startDate와 endDate 사이의 모든 날짜 리스트를 만듦
        List<LocalDate> allDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        List<WorksSummaryResponseDto> queryResult = worksRepository.findWorkSummary(workLocationSi, workLocationGu, workLocationDong,
                workTypeDetailId, workTypeId, startDate, endDate);

        // 3. 존재하는 데이터 기반으로 HashMap에 날짜별로 workCount를 저장
        Map<LocalDate, Long> workCountMap = queryResult.stream()
                .collect(Collectors.toMap(WorksSummaryResponseDto::getDate, WorksSummaryResponseDto::getWorkCount));

        // 4. 모든 날짜에 대해 없는 날짜는 workCount = 0으로 추가
        List<WorksSummaryResponseDto> result = new ArrayList<>();
        for (LocalDate date : allDates) {
            result.add(new WorksSummaryResponseDto(date, workCountMap.getOrDefault(date, 0L)));
        }

        // 필터를 적용한 데이터 조회
        return result;
    }

}
