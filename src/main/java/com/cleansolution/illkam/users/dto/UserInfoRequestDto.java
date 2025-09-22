package com.cleansolution.illkam.users.dto;

// New Feature 12: Information exposure restriction and chatroom entry filtering

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoRequestDto {
    private Long user1Id;  // The user requesting the information
    private Long user2Id;  // The user whose information is being requested
    private Long workId;   // Optional work ID for permission check
}