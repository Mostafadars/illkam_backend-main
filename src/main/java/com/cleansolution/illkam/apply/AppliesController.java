package com.cleansolution.illkam.apply;


import com.cleansolution.illkam.apply.dto.AppliesResponseDto;
import com.cleansolution.illkam.apply.dto.AppliesSaveRequestDto;
import com.cleansolution.illkam.apply.dto.AppliesUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/apply")
public class AppliesController {
    private final AppliesService appliesService;

    //    TODO : Apply findById
    @PostMapping("/")
    public Long save(@RequestBody AppliesSaveRequestDto requestDto) {
        return appliesService.save(requestDto);
    }


//    확정, 취소 관련
    @PostMapping("/{applyId}/apply")
    public String updateStatus(@PathVariable("applyId") Long applyId, @RequestBody AppliesUpdateDto updateDto) {
        return appliesService.updateStatus(applyId, updateDto);
    }

    @GetMapping("/{id}")
    public AppliesResponseDto findById(@PathVariable Optional<Long> id // 카테고리 필터링
    ) {
        return appliesService.findById(id);
    }

    @GetMapping("/{id}/works")
    public List<AppliesResponseDto> findByWorkId(@PathVariable Optional<Long> id // 카테고리 필터링
    ) {
        return appliesService.findByWorkId(id);
    }


    //    TODO : Apply findAll
    //    TODO : Apply approve
    //    TODO : Apply cancel
}
