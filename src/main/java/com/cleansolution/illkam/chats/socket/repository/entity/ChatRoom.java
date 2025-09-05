package com.cleansolution.illkam.chats.socket.repository.entity;

import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.chats.Chats;
import com.cleansolution.illkam.chats.dto.ChatsSaveRequestDto;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@DynamicUpdate
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChatRoom",uniqueConstraints = {
        @UniqueConstraint(name = "UniqueWorkAndApplierAndRequester", columnNames = {"employee_id", "employer_id" , "work_id"}),
})
public class ChatRoom  extends BaseTimestampEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;

    //단방향
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "lastChatMesgId")
    private ChatMessage lastChatMesg;

    @OneToMany(mappedBy = "roomId", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatMessage> messages;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Users employee;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "employer_id")
    private Users employer;

    @ManyToOne
    @JsonIgnore
    @Setter
    @JoinColumn(name = "work_id")
    private Works work;

    private LocalDate workDate;
    private Integer price;

    private String employerName;
    private String employeeName;

    private String employerProfileURL;
    private String employeeProfileURL;
    private String employerPhone;
    private String employeePhone;

    @ColumnDefault("0")
    @Builder.Default
    private Integer employerUnreadCount = 0;

    @ColumnDefault("0")
    @Builder.Default
    private Integer employeeUnreadCount = 0;

    @Setter
    @ColumnDefault("true")
    @Builder.Default
    private Boolean employerExist = true;

    @Setter
    @ColumnDefault("true")
    @Builder.Default
    private Boolean employeeExist = true;


    public static ChatRoom create(Users employer, Users employee,  Works work) {
        ChatRoom room = new ChatRoom();
        room.setId(UUID.randomUUID().toString());
        room.setWork(work);
        room.setPrice(work.getPrice());
        room.setWorkDate(work.getWorkDate());
        room.setEmployeeName(employee.getName());
        room.setEmployeePhone(employee.getPhoneNumber());
        room.setEmployeeProfileURL(employee.getBusinessCertification());
        room.setEmployerName(employer.getName());
        room.setEmployerPhone(employer.getPhoneNumber());
        room.setEmployerProfileURL(employer.getBusinessCertification());
        return room;
    }

    public void addMembers(Users employer, Users employee) {
        this.employer=employer;
        this.employee = employee;
    }

    public void makeChatActive(){
        this.employeeExist = true;
        this.employerExist = true;
    }
}