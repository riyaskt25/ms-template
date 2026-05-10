package com.snb.ms.company;
import com.snb.ms.shared.Users;
import com.snb.ms.shared.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "COMPANY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID")
    private Long companyId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private Users user;

    @Column(name = "REGISTRATION_NUMBER", nullable = false, unique = true, length = 100)
    private String registrationNumber;

    @Column(name = "COMPANY_STATUS", length = 20)
    private String companyStatus;

    @Column(name = "COMPANY_TYPE", length = 50)
    private String companyType;
}
