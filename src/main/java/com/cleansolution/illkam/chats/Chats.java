package com.cleansolution.illkam.chats;

import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.users.Users;
import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@Data
@DynamicUpdate
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueWorkAndApplierAndRequester", columnNames = {"employee_id", "employer_id" , "work_id"}),
})
public class Chats extends BaseTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Users employee;

    @ManyToOne
    @JsonIgnore
//    asdf
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
    @Setter
    @ColumnDefault("true")
    private Boolean employerExist;
    @Setter
    @ColumnDefault("true")
    private Boolean employeeExist;

    public void makeChatActive(){
        this.employeeExist = true;
        this.employerExist = true;
    }
}
