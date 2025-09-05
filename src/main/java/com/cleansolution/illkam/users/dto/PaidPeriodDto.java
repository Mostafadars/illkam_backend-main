package com.cleansolution.illkam.users.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaidPeriodDto {
    private LocalDate paidPeriod;
    private Boolean isPaid;
}
