package com.snb.ms.adminuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.shared.BaseEntity;
import com.snb.ms.shared.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ADMIN_USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AdminUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_USER_ID")
    private Long adminUserId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private Users user;

    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Column(name = "MIDDLE_NAME", length = 100)
    private String middleName;

    @Column(name = "LAST_NAME", length = 100)
    private String lastName;

    @Column(name = "EXTENSION_NUMBER", length = 20)
    private String extensionNumber;

    @Column(name = "SNB_ID", nullable = false, unique = true, length = 50)
    private String snbId;

    @Column(name = "ADMIN_STATUS", length = 20)
    private String adminStatus;
}
