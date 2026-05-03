package com.snb.ms.salesman;
import com.snb.ms.shared.Users;
import com.snb.ms.shared.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "SALESMAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Salesman extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALESMAN_ID")
    private Long salesmanId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private Users user;

    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Column(name = "MIDDLE_NAME", length = 100)
    private String middleName;

    @Column(name = "LAST_NAME", length = 100)
    private String lastName;

    @Column(name = "ACCOUNT_NUMBER", unique = true, length = 50)
    private String accountNumber;

    @Column(name = "CIF_NUMBER", unique = true, length = 50)
    private String cifNumber;

    @Column(name = "ID_NUMBER", unique = true, length = 50)
    private String idNumber;

    @Column(name = "AVAILABLE_INCENTIVE_AMOUNT", precision = 15, scale = 2)
    private BigDecimal availableIncentiveAmount;
}
