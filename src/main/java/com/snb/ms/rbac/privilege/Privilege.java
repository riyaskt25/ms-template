package com.snb.ms.rbac.privilege;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "PRIVILEGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Privilege extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRIVILEGE_ID")
    private Long privilegeId;

    @Column(name = "PRIVILEGE_CODE", nullable = false, unique = true, length = 100)
    private String privilegeCode;

    @Column(name = "PRIVILEGE_NAME", nullable = false, unique = true, length = 150)
    private String privilegeName;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;
}
