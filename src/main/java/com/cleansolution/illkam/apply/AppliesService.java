package com.cleansolution.illkam.apply;


import com.cleansolution.illkam.apply.dto.*;
import com.cleansolution.illkam.firebase.FCMService;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.users.UsersRepository;
import com.cleansolution.illkam.works.Works;
import com.cleansolution.illkam.works.WorksRepository;
import com.cleansolution.illkam.works.dto.WorksResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppliesService {
    private final AppliesRepository appliesRepository;
    private final WorksRepository worksRepository;
    private final UsersRepository usersRepository;
    private final FCMService fcmService;

    //    TODO : Apply approve
    //    TODO : Apply cancel

    @Transactional
    Long save(AppliesSaveRequestDto requestDto) {
        Works works = worksRepository.findById(requestDto.getWork_id()).orElseThrow(() -> new IllegalArgumentException("해당 일깜이 없습니다."));
        Users users = usersRepository.findById(requestDto.getApplier_id()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        requestDto.updateUsersAndWorks(users, works);
        try {
            fcmService.sendMessage(
                    works.getEmployer(),
                    "일깜 신청",
                    users.getName() + "님이 일깜에 신청하셨어요.",
                    "work", works.getId().toString());
        } catch (Exception e) {
            System.out.println(e);
        }

        return appliesRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    String updateStatus(Long applyId, AppliesUpdateDto updateDto) {
        Applies apply = appliesRepository.findById(applyId).orElseThrow(() -> new IllegalArgumentException("해당 지원이없습니다."));
        apply.update(updateDto.getStatus());
        try {
            Users aplier = apply.getApplier();
            Works work = apply.getWorks();
            if (updateDto.getStatus() == 1) {
                work.updateWorkStatus(true, aplier);

                work.getAppliesList().forEach((elem) -> {
                    if (!Objects.equals(elem.getApplier().getId(), aplier.getId())) {
                        try {
                            fcmService.sendMessage(elem.getApplier(), "일깜 만료", "신청하신 일깜에서 다른 신청자가 확정되었어요.", "work", work.getId().toString());
                        } catch (Exception e) {
                            System.err.println("FCM 전송 실패: " + elem.getApplier().getId() + " - " + e.getMessage());
                        }
                    } else {
                        try {
                            fcmService.sendMessage(aplier, "일깜 확정", "신청하신 일깜에서 선정되셨어요.", "work", work.getId().toString());
                        } catch (Exception e) {
                            System.err.println("FCM 전송 실패: " + elem.getApplier().getId() + " - " + e.getMessage());
                        }
                    }
                });
            }
            if (updateDto.getStatus() == 9) {
//                일깜 고용자를 없애줘야 함
                work.updateWorkStatus(false, null);
                fcmService.sendMessage(aplier, "일깜 확정 취소", "확정된 일깜이 취소되었어요.", "work", work.getId().toString());
            }
            worksRepository.save(work);

        } catch (Exception e) {
            System.out.println(e);
        }
        return updateDto.getStatus() == 9 ? "cancel" : "approve";
    }

    public AppliesResponseDto findById(Optional<Long> id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("파라미터가 존재하지 않습니다.");
        }

        Applies entity = appliesRepository.findById(id.get())
                .orElseThrow(() -> new IllegalArgumentException("해당 지원자가 없습니다. id=" + id));
        return new AppliesResponseDto(entity);
    }

    public List<AppliesResponseDto> findByWorkId(Optional<Long> workId) {
        if (workId.isEmpty()) {
            throw new IllegalArgumentException("파라미터가 존재하지 않습니다.");
        }
        Works works = worksRepository.findById(workId.get()).orElseThrow(() -> new IllegalArgumentException("해당 일깜이 존재하지 않습니다."));
        return appliesRepository.findByWorks(works)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AppliesResponseDto convertToDTO(Applies applies) {
        return new AppliesResponseDto(applies);
    }


    // New Feature 12: Information exposure restriction and chatroom entry filtering
    public IsAppliedResponseDto isApplied(IsAppliedRequestDto requestDto) {
        // Find all applies for the given work
        Works work = worksRepository.findById(requestDto.getWorkId())
                .orElseThrow(() -> new IllegalArgumentException("해당 일깜이 존재하지 않습니다."));

        List<Applies> appliesList = appliesRepository.findByWorks(work);

        // Check if the applier has applied for this work
        Optional<Applies> userApply = appliesList.stream()
                .filter(apply -> apply.getApplier().getId().equals(requestDto.getApplierId()))
                .findFirst();

        if (userApply.isPresent()) {
            return new IsAppliedResponseDto(true, userApply.get().getStatus());
        } else {
            return new IsAppliedResponseDto(false, null);
        }
    }
}
