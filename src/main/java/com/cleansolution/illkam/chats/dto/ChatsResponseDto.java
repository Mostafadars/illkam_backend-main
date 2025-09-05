package com.cleansolution.illkam.chats.dto;

import com.cleansolution.illkam.chats.socket.repository.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatsResponseDto {
    private String id;

    private LocalDate workDate;
    private Integer price;
    private String lastChatMsg;
    private LocalDateTime lastChatTime;
    private String address;
    private String workType;
    private Long workId;
    private String workLocation;

    private String employerName;
    private String employeeName;
    private String employerPhone;
    private String employeePhone;
    private String employerProfile;
    private String employeeProfile;
    private Boolean employerExist;
    private Boolean employeeExist;
    private Long employerId;
    private Long employeeId;

    private LocalDateTime modifiedDateTime;

    public ChatsResponseDto(ChatRoom entity) {
        this.id = entity.getId();
        this.workDate = entity.getWorkDate();
        this.workId = entity.getWork().getId();
        this.price = entity.getPrice();
        this.workLocation = entity.getWork().getWorkLocationSi();
        this.lastChatMsg = entity.getLastChatMesg() != null ? entity.getLastChatMesg().getMessage() : null;
        this.lastChatTime = entity.getLastChatMesg() != null ? entity.getLastChatMesg().getCreatedAt():null;
        this.address = entity.getWork().getWorkLocationSi();
        this.workType = entity.getWork().getWorkTypes().getName();
        this.employerName = entity.getEmployerName();
        this.employeeName = entity.getEmployeeName();
        this.employerPhone = entity.getEmployerPhone();
        this.employeePhone = entity.getEmployeePhone();
        this.employerProfile =entity.getEmployerProfileURL();
        this.employeeProfile =entity.getEmployeeProfileURL();
        this.employeeExist = entity.getEmployeeExist();
        this.employerExist = entity.getEmployerExist();
        this.employeeId = entity.getEmployee().getId();
        this.employerId = entity.getEmployer().getId();
        this.modifiedDateTime = entity.getModifiedDateTime();
    }
}
