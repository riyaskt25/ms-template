package com.snb.ms.rbac.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.snb.ms.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ROLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Role extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ROLE_ID")
  private Long roleId;

  @Column(name = "ROLE_CODE", nullable = false, unique = true, length = 100)
  private String roleCode;

  @Column(name = "ROLE_NAME", nullable = false, unique = true, length = 150)
  private String roleName;

  @Column(name = "DESCRIPTION", length = 255)
  private String description;
}
