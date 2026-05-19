package com.snb.ms.rbac.roleprivilege;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.rbac.privilege.Privilege;
import com.snb.ms.rbac.role.Role;
import com.snb.ms.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ROLE_PRIVILEGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RolePrivilege extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIVILEGE_ID", nullable = false)
    private Privilege privilege;
}
