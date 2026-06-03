package com.snb.ms.exception;

import lombok.Getter;

@Getter
public class BusinessValidationException extends RuntimeException {

  private final ErrorCodeEnum errorCode;
  private final String description;
  private final String descriptionKey;
  private final Object[] descriptionArgs;

  public BusinessValidationException(
      String descriptionKey, Object[] descriptionArgs, String fallbackDescription) {
    this(ErrorCodeEnum.INVALID_ARGUMENT, descriptionKey, descriptionArgs, fallbackDescription);
  }

  public BusinessValidationException(
      ErrorCodeEnum errorCode,
      String descriptionKey,
      Object[] descriptionArgs,
      String fallbackDescription) {
    super(fallbackDescription);
    this.errorCode = errorCode;
    this.description = fallbackDescription;
    this.descriptionKey = descriptionKey;
    this.descriptionArgs = descriptionArgs;
  }

  public static BusinessValidationException invalidCompanyStatusValue(Object value) {
    return new BusinessValidationException(
        "error.company.status.invalid.value",
        new Object[] {value},
        "Invalid company status: " + value + ". Allowed values are ACTIVE or REJECTED.");
  }

  public static BusinessValidationException invalidCompanyStatusTransition(Object from, Object to) {
    return new BusinessValidationException(
        "error.company.status.invalid.transition",
        new Object[] {from, to},
        "Company status transition is not allowed from " + from + " to " + to + ".");
  }

  public static BusinessValidationException invalidSortField(Object value) {
    return new BusinessValidationException(
        "error.validation.sort.field.invalid",
        new Object[] {value},
        "Invalid sortBy field: " + value + ".");
  }

  public static BusinessValidationException invalidSortDirectionCount(
      int fieldCount, int directionCount) {
    return new BusinessValidationException(
        "error.validation.sort.direction.count.invalid",
        new Object[] {fieldCount, directionCount},
        "sortDirection count must match sortBy count. sortBy items="
            + fieldCount
            + ", sortDirection items="
            + directionCount
            + ".");
  }

  public static BusinessValidationException invalidSortDirectionValue(Object value) {
    return new BusinessValidationException(
        "error.validation.sort.direction.value.invalid",
        new Object[] {value},
        "Invalid sortDirection value: " + value + ". Allowed values are ASC or DESC.");
  }

  public static BusinessValidationException activeCompanySalesmanInvitationExists(
      Object companyId, Object emailAddress, Object mobileNumber, Object idNumber) {
    return new BusinessValidationException(
        "error.company.salesman.invitation.active.exists",
        new Object[] {companyId, emailAddress, mobileNumber, idNumber},
        "An open salesman invitation already exists for companyId="
            + companyId
            + " with the provided email, mobile number, or ID number.");
  }
}
