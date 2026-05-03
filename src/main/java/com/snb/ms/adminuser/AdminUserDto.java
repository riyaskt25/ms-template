package com.snb.ms.adminuser;
import com.snb.ms.shared.BaseResponseDTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDto extends BaseResponseDTO {
    private Long adminUserId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String extensionNumber;
    private String adminType;
    private String adminStatus;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
