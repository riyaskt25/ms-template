package com.snb.ms.exception;

import com.snb.ms.shared.BaseResponseDTO;
import com.snb.ms.shared.ErrorInfo;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        return buildResponse(ex.getErrorCode(), ex.getDescription());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Invalid argument: {}", ex.getMessage(), ex);
        return buildResponse(ErrorCodeEnum.INVALID_ARGUMENT, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        log.error("Validation failed with {} field errors", ex.getBindingResult().getFieldErrors().size(), ex);
        List<ErrorInfo> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toErrorInfo)
                .toList();
        BaseResponseDTO response = new BaseResponseDTO(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Constraint validation failed with {} errors", ex.getConstraintViolations().size(), ex);
        List<ErrorInfo> errors = ex.getConstraintViolations().stream()
            .map(violation -> new ErrorInfo(
                "VALIDATION_ERROR",
                violation.getPropertyPath().toString().toUpperCase().replace('.', '_') + "_INVALID",
                violation.getMessage(),
                "Rejected value: " + violation.getInvalidValue()
            ))
            .toList();
        BaseResponseDTO response = new BaseResponseDTO(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponseDTO> handleInvalidJson(HttpMessageNotReadableException ex) {
        log.error("Malformed JSON request", ex);
        return buildResponse(ErrorCodeEnum.MALFORMED_JSON, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation");
        log.debug("Data integrity root cause", ex);
        return buildResponse(ErrorCodeEnum.DATA_INTEGRITY_VIOLATION, resolveDataIntegrityDescription(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDTO> handleGeneric(Exception ex) {
        log.error("Unexpected internal server error", ex);
        return buildResponse(ErrorCodeEnum.INTERNAL_ERROR, null);
    }

    private ResponseEntity<BaseResponseDTO> buildResponse(ErrorCodeEnum errorCode, String description) {
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("Building error response status={} code={} locale={}", errorCode.getStatus().value(), errorCode.name(), locale);
        String message = messageSource.getMessage(errorCode.getMessageKey(), null, errorCode.getMessageKey(), locale);
        ErrorInfo error = new ErrorInfo(errorCode.getType(), errorCode.name(), message, description);
        BaseResponseDTO response = new BaseResponseDTO(List.of(error));
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    private String resolveDataIntegrityDescription(DataIntegrityViolationException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String raw = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String normalized = raw == null ? "" : raw.toLowerCase(Locale.ROOT);

        if (normalized.contains("uk_users_email") || normalized.contains("users(email_address")) {
            return messageSource.getMessage("error.data.integrity.duplicate.email", null, "A user with this email already exists.", locale);
        }
        if (normalized.contains("uk_users_mobile") || normalized.contains("users(mobile_number")) {
            return messageSource.getMessage("error.data.integrity.duplicate.mobile", null, "A user with this mobile number already exists.", locale);
        }
        if (normalized.contains("uk_company_reg") || normalized.contains("company(registration_number")) {
            return messageSource.getMessage("error.data.integrity.duplicate.registrationNumber", null, "A company with this registration number already exists.", locale);
        }
        if (normalized.contains("uk_salesman_acc") || normalized.contains("salesman(account_number")) {
            return messageSource.getMessage("error.data.integrity.duplicate.accountNumber", null, "A salesman with this account number already exists.", locale);
        }
        if (normalized.contains("uk_salesman_cif") || normalized.contains("salesman(cif_number")) {
            return messageSource.getMessage("error.data.integrity.duplicate.cifNumber", null, "A salesman with this CIF number already exists.", locale);
        }
        if (normalized.contains("uk_salesman_id") || normalized.contains("salesman(id_number")) {
            return messageSource.getMessage("error.data.integrity.duplicate.idNumber", null, "A salesman with this ID number already exists.", locale);
        }
        return messageSource.getMessage("error.data.integrity.generic", null, "Duplicate or conflicting data was found.", locale);
    }

    private ErrorInfo toErrorInfo(FieldError fieldError) {
        return new ErrorInfo(
                "VALIDATION_ERROR",
                fieldError.getField().toUpperCase() + "_INVALID",
                fieldError.getDefaultMessage() == null ? fieldError.getField() + " is invalid" : fieldError.getDefaultMessage(),
                "Rejected value: " + fieldError.getRejectedValue()
        );
    }
}
