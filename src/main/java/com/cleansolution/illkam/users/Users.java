// package com.cleansolution.illkam.users;

// import com.cleansolution.illkam.base.BaseTimestampEntity;
// import com.cleansolution.illkam.users.dto.PaidPeriodDto;
// import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
// import com.cleansolution.illkam.works.Works;
// import com.cleansolution.illkam.works.images.WorkImages;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import jakarta.persistence.*;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.experimental.SuperBuilder;
// import org.hibernate.annotations.ColumnDefault;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.stream.Collectors;

// @Entity
// @Getter
// @NoArgsConstructor
// @SuperBuilder
// public class Users extends BaseTimestampEntity {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(length = 20, nullable = false)
//     private String name;

//     @Column(length = 20, nullable = false)
//     private String phoneNumber;

//     @Column(length = 30, nullable = true)
//     private String businessNumber;

//     @Column(length = 50, nullable = false)
//     private String email;

//     @Column(length = 50, nullable = false)
//     private String password;

//     @Column
//     private Boolean isApproved;

//     private LocalDateTime approveDate;

//     private String businessAddress;

//     private String businessCertification;

//     @Setter
//     private String fcmToken;

//     //    리뷰 관리, 리뷰 평점, 리뷰 숫자
//     @Builder.Default
//     private Double rating = 0.0;
//     @Builder.Default
//     private Integer reviewCount = 0;
//     @Builder.Default
//     private Integer writeCount = 0;

//     //    유료 회원 관리
//     private LocalDate paidPeriod;
//     @ColumnDefault("FALSE")
//     @Builder.Default
//     private Boolean isPaidUser = false;

//     @JsonIgnore
//     @OneToMany(mappedBy = "employer")
//     private List<Works> worksList;

//     @Builder
//     public Users(
//             String name,
//             String businessAddress,
//             String businessNumber,
//             String phoneNumber,
//             String email,
//             String password,
//             String businessCertification
//     ) {
//         this.name = name;
//         this.businessNumber = businessAddress;
//         this.businessAddress = businessNumber;
//         this.phoneNumber = phoneNumber;
//         this.password = password;
//         this.email = email;
//         this.businessCertification = businessCertification;
//     }

//     //    TODO 유저 수정 운영방침 논의 필요
//     public void update(String businessNumber, String phoneNumber, String email) {
//         this.businessAddress = businessNumber;
//         this.phoneNumber = phoneNumber;
//         this.email = email;
//     }

//     public void calculateRating(Integer newRating, Integer totalCount) {
//         this.rating = (this.rating * (totalCount-1) + newRating)/totalCount;
//         this.reviewCount = totalCount;
//     }

//     public void removeReview(Integer newRating) {
//         Double total = this.rating * this.reviewCount;
//         this.reviewCount = this.reviewCount - 1;
//         if(this.reviewCount == 0){
//             this.rating = 0.0;
//         }else{

//         this.rating = (total - newRating)/ this.reviewCount;
//         }
//     }

//     public void addWriteReview(){
//         this.reviewCount += 1;
//     }

//     public void minusWriteReview(){

//         this.reviewCount -= 1;
//     }

//     private Integer convertToInt(WorkReviews reviews) {
//         return reviews.getStarCount();
//     }

//     public void setApprove(PaidPeriodDto dto){
//         this.isPaidUser = dto.getIsPaid();
//         this.paidPeriod = dto.getPaidPeriod();

//     }

// }

package com.cleansolution.illkam.users;

import com.cleansolution.illkam.base.BaseTimestampEntity;
import com.cleansolution.illkam.users.dto.PaidPeriodDto;
import com.cleansolution.illkam.works.WorkReviews.WorkReviews;
import com.cleansolution.illkam.works.Works;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder // This is the only builder annotation needed
public class Users extends BaseTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String phoneNumber;

    @Column(length = 30, nullable = true)
    private String businessNumber;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;

    @Column
    private Boolean isApproved;

    private LocalDateTime approveDate;

    private String businessAddress;

    private String businessCertification;

    @Setter
    private String fcmToken;

    // Your use of @Builder.Default is perfect! It will work with @SuperBuilder.
    @Builder.Default
    private Double rating = 0.0;
    @Builder.Default
    private Integer reviewCount = 0;
    @Builder.Default
    private Integer writeCount = 0;

    private LocalDate paidPeriod;
    @ColumnDefault("FALSE")
    @Builder.Default
    private Boolean isPaidUser = false;

    @JsonIgnore
    @OneToMany(mappedBy = "employer")
    private List<Works> worksList;

    // New Update: Privacy Policy Feature
    @Column
    @Builder.Default
    private Boolean marketingConsent = null; // null means not specified, true means consented

    @Column
    @Builder.Default
    private Boolean orderConsent = null; // null means not specified, true means consented

    // New Feature 2: Admin login and logout
    @Column
    @Builder.Default
    private Boolean isAdmin = false; // Add this field to identify admin users

    // The constructor with @Builder has been removed to resolve the conflict.

    public void update(String businessNumber, String phoneNumber, String email) {
        this.businessAddress = businessNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void calculateRating(Integer newRating, Integer totalCount) {
        this.rating = (this.rating * (totalCount - 1) + newRating) / totalCount;
        this.reviewCount = totalCount;
    }

    public void removeReview(Integer newRating) {
        Double total = this.rating * this.reviewCount;
        this.reviewCount = this.reviewCount - 1;
        if (this.reviewCount == 0) {
            this.rating = 0.0;
        } else {
            this.rating = (total - newRating) / this.reviewCount;
        }
    }

    public void addWriteReview() {
        this.reviewCount += 1;
    }

    public void minusWriteReview() {
        this.reviewCount -= 1;
    }

    private Integer convertToInt(WorkReviews reviews) {
        return reviews.getStarCount();
    }

    public void setApprove(PaidPeriodDto dto) {
        this.isPaidUser = dto.getIsPaid();
        this.paidPeriod = dto.getPaidPeriod();
    }

    public void setMarketingConsent(Boolean marketingConsent) {
        this.marketingConsent = marketingConsent;
    }

    public void setOrderConsent(Boolean orderConsent) {
        this.orderConsent = orderConsent;
    }
}