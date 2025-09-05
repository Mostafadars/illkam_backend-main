package com.cleansolution.illkam.works.WorkTypes;

import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetails;
import com.cleansolution.illkam.works.WorkTypes.detail.WorkTypeDetailsRepository;
import com.cleansolution.illkam.works.WorkTypes.dto.WorkTypeDetailsResponseDto;
import com.cleansolution.illkam.works.WorkTypes.dto.WorkTypeDetailsSaveRequestDto;
import com.cleansolution.illkam.works.WorkTypes.dto.WorkTypesResponseDto;
import com.cleansolution.illkam.works.WorkTypes.dto.WorksTypesSaveRequestDto;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypes;
import com.cleansolution.illkam.works.WorkTypes.main.WorkTypesRepository;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.dto.WorksResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkTypesService {
    private final WorkTypesRepository workTypesRepository;
    private final WorkTypeDetailsRepository workTypeDetailsRepository;

    @Transactional
    public Long saveMainCategory(WorksTypesSaveRequestDto requestDto) {
        return workTypesRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long editMainCategoryInputInfo(WorksTypesSaveRequestDto requestDto) {
        WorkTypes wt = workTypesRepository.findById(requestDto.getId()).orElseThrow(()-> new IllegalArgumentException(""));
        wt.updateInfo(requestDto.getWorkInfoDetail());
        wt.updateName(requestDto.getName());
        return workTypesRepository.save(wt).getId();
    }
    @Transactional
    public Boolean removeType(Long typeId) {
        workTypesRepository.deleteById(typeId);
        return true;
    }

    @Transactional
    public Long editDetailCategoryDetail(Long detailId, WorkTypeDetailsSaveRequestDto requestDto) {
        WorkTypeDetails workTypeDetails = workTypeDetailsRepository.findById(detailId).orElseThrow(()-> new IllegalArgumentException(""));
        workTypeDetails.updateName(requestDto.getName());
        workTypeDetailsRepository.save(workTypeDetails);
        return workTypeDetails.getId();
    }

    @Transactional
    public void deleteCategoryDetail(Long detailId){
        workTypeDetailsRepository.deleteById(detailId);
    }

    @Transactional
    public Long saveDetailCategory(WorkTypeDetailsSaveRequestDto requestDto) {
        requestDto.updateWorkType(workTypesRepository.findById(requestDto.getWorkTypesId()).orElseThrow(() -> new IllegalArgumentException("해당 일깜 타입 메인 카테고리가 없습니다. id=" + requestDto.getWorkTypesId())));

        return workTypeDetailsRepository.save(requestDto.toEntity()).getId();
    }

    public List<WorkTypesResponseDto> findAllCategories(){
        return workTypesRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private WorkTypesResponseDto convertToDTO(WorkTypes workTypes) {
        return new WorkTypesResponseDto(workTypes);
    }

    public WorkTypesResponseDto findMainCategoryById(Long id){
        return new WorkTypesResponseDto(workTypesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 일깜 타입 메인 카테고리가 없습니다. id=" + id)));
    }

    public WorkTypeDetailsResponseDto findDetailCategoryById(Long id){
        return new WorkTypeDetailsResponseDto(workTypeDetailsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 일깜 타입 디테일 카테고리가 없습니다. id=" + id)));
    }


}
