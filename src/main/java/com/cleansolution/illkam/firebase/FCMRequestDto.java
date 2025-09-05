package com.cleansolution.illkam.firebase;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMRequestDto {
    private String title, body, targetPageId, routeName;

    @Builder
    public FCMRequestDto(String title, String body, String routeName, String targetPageId){
        this.title = title;
        this.body = body;
        this.routeName = routeName;
        this.targetPageId = targetPageId;
    }
}
