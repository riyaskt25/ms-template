package com.snb.ms.shared;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private Long userId;
    private String emailAddress;
    private String mobileNumber;
    private String userType;
    private Integer failedAttempts;
    private String accountLockedFlag;
    private LocalDateTime lastLoginAt;
    private String accountStatus;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private String deletedFlag;
    private LocalDateTime deletedAt;
    private Long versionNumber;
}
