package com.snb.ms.shared;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersRequest {

    private String emailAddress;

    private String mobileNumber;

    private String userType;

    private String accountStatus;

    private String accountLockedFlag;
}
