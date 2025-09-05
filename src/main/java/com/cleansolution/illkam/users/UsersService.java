package com.cleansolution.illkam.users;

import com.cleansolution.illkam.users.dto.*;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.WorkReviews.WorkReviewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final WorkReviewsRepository workReviewsRepository;

    @Transactional
    public Long login(UserLoginDto loginDto){
        Users user = usersRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword()).orElseThrow(()-> new IllegalArgumentException());
        return user.getId();
    }

    @Transactional
    public Long save(UsersSaveRequestDto requestDto){
        System.out.println(requestDto);
        return usersRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long edit(Long id, UsersSaveRequestDto requestDto){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        entity.update(requestDto.getBusinessAddress(), requestDto.getPhoneNumber(),  requestDto.getEmail());

        return usersRepository.save(entity).getId();
    }

    @Transactional
    public Long updateFcm(Long id, UsersSaveRequestDto requestDto){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        entity.setFcmToken(requestDto.getFcmToken());
        return usersRepository.save(entity).getId();
    }

    public UsersResponseDto findById (Long id){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(entity);
        return new UsersResponseDto(entity,reviews);
    }

    public ApplierResponseDto findApplierById (Long id){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(entity);
        return new ApplierResponseDto(entity, reviews);
    }

    public EmployerResponseDto findEmployerById (Long id){
        Users entity = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id="+id));
        List<WorkReviews> reviews = workReviewsRepository.findAllByTarget(entity);
        return new EmployerResponseDto(entity, reviews);
    }
}
