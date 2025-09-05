package com.cleansolution.illkam.works;


import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import com.cleansolution.illkam.works.WorkReviews.WorkReviewService;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkReviews.WorkReviewsRepository;
import com.cleansolution.illkam.works.WorkTypes.WorkTypesService;
import com.cleansolution.illkam.works.WorkTypes.dto.WorkTypeDetailsResponseDto;
import com.cleansolution.illkam.works.WorkTypes.dto.WorkTypeDetailsSaveRequestDto;
import com.cleansolution.illkam.works.WorkTypes.dto.WorkTypesResponseDto;
import com.cleansolution.illkam.works.WorkTypes.dto.WorksTypesSaveRequestDto;
import com.cleansolution.illkam.works.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/works")
public class WorksController {
//    TODO 일깜을 등록한 상황에서 수정이 발생했을 경우 기존 일깜 지원자들에 대해 어떻게 처리하는 게 좋을까?
//    TODO 예를 들어 일깜의 가격이 변동했을 수 있음

    private final WorksService worksService;
    private final WorkReviewService workReviewService;
    private final WorkTypesService workTypesService;
    private final WorksRepository worksRepository;
    private final UsersRepository usersRepository;
    private final WorkReviewsRepository workReviewsRepository;

    @GetMapping("/{id}/employer")
    public List<WorksResponseDto> findByEmployerId(@PathVariable("id") Long id) {
        return worksService.findByEmployer(id);
    }

    @GetMapping("/{id}/employee")
    public List<WorksResponseDto> findByEmployeeId(@PathVariable("id") Long id) {
        return worksService.findByEmployee(id);
    }

    @GetMapping("/filters")
    public Page<WorksResponseDto> getProductsByFilters(
            @RequestParam(name = "workLocationSi",required = false) String workLocationSi,
            @RequestParam(name = "workLocationGu",required = false) String workLocationGu,
            @RequestParam(name = "workLocationDong",required = false) String workLocationDong,
            @RequestParam(name = "workTypeDetailId",required = false) Long workTypeDetailId,
            @RequestParam(name = "workTypeId",required = false) Long workTypeId,
            @RequestParam(name = "workDate") LocalDate workDate,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "10") int size) {
        Sort sort = Sort.by("createdDateTime").descending();

        Pageable pageable = PageRequest.of(page, size,sort);
        return worksService.getWorksByFilters(
                workLocationSi,
                workLocationGu,
                workLocationDong,
                workTypeDetailId,
                workTypeId,
                workDate,
                pageable);
    }

    @GetMapping("/summary")
    public List<WorksSummaryResponseDto> getWorkSummary(
            @RequestParam(required = false) String workLocationSi,
            @RequestParam(required = false) String workLocationGu,
            @RequestParam(required = false) String workLocationDong,
            @RequestParam(required = false) Long workTypeDetailId,
            @RequestParam(required = false) Long workTypeId) {
        // 서비스에서 결과 조회
        return worksService.getWorkSummary(workLocationSi, workLocationGu, workLocationDong, workTypeDetailId, workTypeId);
    }

    @GetMapping("/summary/monthly")
    public List<WorksSummaryResponseDto> getWorkSummaryByMonthly(
            @RequestParam(required = false) String workLocationSi,
            @RequestParam(required = false) String workLocationGu,
            @RequestParam(required = false) String workLocationDong,
            @RequestParam(required = false) Long workTypeDetailId,
            @RequestParam(required = false) Long workTypeId,
            @RequestParam(required = true) LocalDate month
            ) {

        // 서비스에서 결과 조회
        return worksService.getWorkSummaryByMonthly(workLocationSi, workLocationGu, workLocationDong, workTypeDetailId, workTypeId,month);
    }


    @PostMapping(value = "/", consumes = {"application/json;charset=UTF-8"})
    public Long save(@RequestBody WorksSaveRequestDto requestDto) {
        return worksService.save(requestDto);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteById(@PathVariable("id") Long id) {
        return worksService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Long updateById(@PathVariable("id") Long id, @RequestBody WorksSaveRequestDto dto) {
        return worksService.updateById(id,dto);
    }


    @PostMapping(value = "/type")
    public Long saveTypes(@RequestBody WorksTypesSaveRequestDto requestDto) {
        return workTypesService.saveMainCategory(requestDto);
    }

    @PutMapping(value = "/type")
    public Long editTypes(@RequestBody WorksTypesSaveRequestDto requestDto) {
        return workTypesService.editMainCategoryInputInfo(requestDto);
    }

    @DeleteMapping(value = "/type/{typeId}")
    public Boolean removeType(@PathVariable Long typeId) {
        return workTypesService.removeType(typeId);
    }

    @PutMapping(value = "/type/detail/{detailId}")
    public Long editDetailName(@PathVariable Long detailId, @RequestBody WorkTypeDetailsSaveRequestDto requestDto) {
        return workTypesService.editDetailCategoryDetail(detailId,requestDto);
    }

    @DeleteMapping(value = "/type/detail/{detailId}")
    public Boolean editTypes(@PathVariable Long detailId ) {
        workTypesService.deleteCategoryDetail(detailId);
        return true;
    }

    @PostMapping(value = "/type/detail")
    public Long saveDetailTypes(@RequestBody WorkTypeDetailsSaveRequestDto requestDto) {
        return workTypesService.saveDetailCategory(requestDto);
    }

    @PostMapping("/date/summary")
    public List<WorksSummaryResponseDto> getWorkSummary(@RequestBody WorksDateResponseDto requestDto) {
        // String으로 받은 데이터를 LocalDateTime으로 파싱
        return worksService.getWorkSummary(requestDto.getWorkDate());
    }

    @PostMapping("/date")
    public List<WorksResponseDto> findByDate(@RequestBody WorksDateResponseDto requestDto) {

        System.out.println(requestDto.getWorkDate());
        return worksService.findByDate(requestDto.getWorkDate());
    }




//    리뷰 남기기
    @PostMapping("/{id}/review")
    public Long writeWorkReviews(@PathVariable("id") Long id, @RequestBody WorkReviewSaveDto requestDto) {
        Works works = worksRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 일깜이 존재하지 않습니다." + id));
        works.updateWorkAsReviewed();
        worksRepository.save(works);
        Users writer = usersRepository.findById(requestDto.getWriter()).orElseThrow(()-> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다." + id));
        Users target = usersRepository.findById(requestDto.getTarget()).orElseThrow(()-> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다." + id));
        return workReviewService.writeReview(works,writer, target, requestDto);
    }

    @PutMapping("/review/{reviewId}")
    public Long editWorkReview(@PathVariable("reviewId") Long reviewId, @RequestBody WorkReviewSaveDto requestDto) {
        WorkReviews workReviews = workReviewsRepository.findById(reviewId).orElseThrow(()-> new IllegalArgumentException("리뷰 가 존재하지 않습니다."));
        return workReviewService.editReview(workReviews, requestDto);
    }



    @DeleteMapping("/{id}/review")
    public Boolean deleteWorkReviews(@PathVariable("id") Long id) {
        return workReviewService.deleteReview(id);
    }

    @GetMapping("/type")
    public List<WorkTypesResponseDto> findAllCategories() {
        return workTypesService.findAllCategories();
    }



    @GetMapping("/{id}")
    public WorksResponseDto findById(@PathVariable("id") Long id) {
        return worksService.findById(id);
    }


    @GetMapping("/")
    public List<WorksResponseDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String workLocationSi,
            @RequestParam(required = false) Long workTypeId,
            @RequestParam(required = false) Long workTypeDetailId
    ) {
        Sort sort = Sort.by("createdDateTime").descending();

        Pageable pageable = PageRequest.of(page, size,sort);
        return worksService.findAll(workLocationSi,workTypeId,workTypeDetailId, pageable);
    }

    @CrossOrigin(origins = "http://localhost:3000") // 허용할 Origin 지정
    @GetMapping("/admin")
    public Page<WorksResponseDto> findAdminAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String workLocationSi,
            @RequestParam(required = false) Long workTypeId,
            @RequestParam(required = false) Long workTypeDetailId
    ) {
        Sort sort = Sort.by("createdDateTime").descending();

        Pageable pageable = PageRequest.of(page, size,sort);
        return worksService.findAdminAll(workLocationSi,workTypeId,workTypeDetailId, pageable);
    }

    @GetMapping("/{id}/type")
    public WorkTypesResponseDto findTypeById(@PathVariable("id") Long id) {
        return workTypesService.findMainCategoryById(id);
    }


    @GetMapping("/{id}/type/detail")
    public WorkTypeDetailsResponseDto findDetailTypeById(@PathVariable("id") Long id) {
        return workTypesService.findDetailCategoryById(id);
    }




}
