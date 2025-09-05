package com.cleansolution.illkam.works.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WorksSummaryResponseDto{
    private LocalDate date;
    private long workCount;

    public WorksSummaryResponseDto(LocalDate date, long workCount) {
        this.date = date;
        this.workCount = workCount;
    }

    // Getter and Setter
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getWorkCount() {
        return workCount;
    }

    public void setWorkCount(long workCount) {
        this.workCount = workCount;
    }
}
