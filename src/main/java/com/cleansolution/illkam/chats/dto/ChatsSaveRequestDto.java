package com.cleansolution.illkam.chats.dto;

import com.cleansolution.illkam.chats.Chats;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatsSaveRequestDto {
    private Long employee_id;
    private Long work_id;

    @Builder
    ChatsSaveRequestDto(
            Long employee_id,
            Long work_id
    ){
        this.employee_id =employee_id;
        this.work_id = work_id;
    }

    public Chats toEntity(
            Users employer,
            Users employee,
            Works work
    ){
        return Chats.builder()
                .employee(employee)
                .employer(employer)
                .work(work)
                .workDate(work.getWorkDate())
                .price(work.getPrice())
                .employeeName(employee.getName())
                .employeeProfileURL(employee.getBusinessCertification())
                .employeePhone(employee.getPhoneNumber())
                .employeeExist(true)
                .employerName(employer.getName())
                .employerProfileURL(employer.getBusinessCertification())
                .employerPhone(employer.getPhoneNumber())
                .employerExist(true)
                .build();

    }
}
