package com.snb.ms.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

  private final ErrorCodeEnum errorCode;
  private final String description;
  private final String descriptionKey;
  private final Object[] descriptionArgs;

  public ResourceNotFoundException(String message) {
    super(message);
    this.errorCode = ErrorCodeEnum.RESOURCE_NOT_FOUND;
    this.description = message;
    this.descriptionKey = null;
    this.descriptionArgs = null;
  }

  public ResourceNotFoundException(
      String descriptionKey, Object[] descriptionArgs, String fallbackDescription) {
    super(fallbackDescription);
    this.errorCode = ErrorCodeEnum.RESOURCE_NOT_FOUND;
    this.description = fallbackDescription;
    this.descriptionKey = descriptionKey;
    this.descriptionArgs = descriptionArgs;
  }

  public static ResourceNotFoundException companyById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.company.not.found.by.id",
        new Object[] {id},
        "Company not found for id=" + id);
  }

  public static ResourceNotFoundException salesmanById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.salesman.not.found.by.id",
        new Object[] {id},
        "Salesman not found for id=" + id);
  }

  public static ResourceNotFoundException adminUserById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.adminUser.not.found.by.id",
        new Object[] {id},
        "Admin user not found for id=" + id);
  }

  public static ResourceNotFoundException adminUserBySnbId(Object snbId) {
    return new ResourceNotFoundException(
        "error.resource.adminUser.not.found.by.snbId",
        new Object[] {snbId},
        "Admin user not found for snbId=" + snbId);
  }

  public static ResourceNotFoundException adminUserByUserId(Object userId) {
    return new ResourceNotFoundException(
        "error.resource.adminUser.not.found.by.userId",
        new Object[] {userId},
        "Admin user not found for userId=" + userId);
  }

  public static ResourceNotFoundException postById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.post.not.found.by.id", new Object[] {id}, "Post not found for id=" + id);
  }

  public static ResourceNotFoundException roleById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.role.not.found.by.id", new Object[] {id}, "Role not found for id=" + id);
  }

  public static ResourceNotFoundException roleByCode(Object roleCode) {
    return new ResourceNotFoundException(
        "error.resource.role.not.found.by.code",
        new Object[] {roleCode},
        "Role not found for code=" + roleCode);
  }

  public static ResourceNotFoundException privilegeById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.privilege.not.found.by.id",
        new Object[] {id},
        "Privilege not found for id=" + id);
  }

  public static ResourceNotFoundException privilegeByCode(Object privilegeCode) {
    return new ResourceNotFoundException(
        "error.resource.privilege.not.found.by.code",
        new Object[] {privilegeCode},
        "Privilege not found for code=" + privilegeCode);
  }

  public static ResourceNotFoundException userRoleById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.userRole.not.found.by.id",
        new Object[] {id},
        "User-role assignment not found for id=" + id);
  }

  public static ResourceNotFoundException userRoleByUserIdAndRoleId(Object userId, Object roleId) {
    return new ResourceNotFoundException(
        "error.resource.userRole.not.found.by.userId.and.roleId",
        new Object[] {userId, roleId},
        "User-role assignment not found for userId=" + userId + " and roleId=" + roleId);
  }

  public static ResourceNotFoundException userRoleByUserIdAndRoleCode(
      Object userId, Object roleCode) {
    return new ResourceNotFoundException(
        "error.resource.userRole.not.found.by.userId.and.roleCode",
        new Object[] {userId, roleCode},
        "User-role assignment not found for userId=" + userId + " and roleCode=" + roleCode);
  }

  public static ResourceNotFoundException rolePrivilegeById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.rolePrivilege.not.found.by.id",
        new Object[] {id},
        "Role-privilege grant not found for id=" + id);
  }

  public static ResourceNotFoundException rolePrivilegeByRoleIdAndPrivilegeId(
      Object roleId, Object privilegeId) {
    return new ResourceNotFoundException(
        "error.resource.rolePrivilege.not.found.by.roleId.and.privilegeId",
        new Object[] {roleId, privilegeId},
        "Role-privilege grant not found for roleId=" + roleId + " and privilegeId=" + privilegeId);
  }

  public static ResourceNotFoundException rolePrivilegeByRoleCodeAndPrivilegeCode(
      Object roleCode, Object privilegeCode) {
    return new ResourceNotFoundException(
        "error.resource.rolePrivilege.not.found.by.roleCode.and.privilegeCode",
        new Object[] {roleCode, privilegeCode},
        "Role-privilege grant not found for roleCode="
            + roleCode
            + " and privilegeCode="
            + privilegeCode);
  }

  public static ResourceNotFoundException userById(Object id) {
    return new ResourceNotFoundException(
        "error.resource.user.not.found.by.id", new Object[] {id}, "User not found for id=" + id);
  }
}
