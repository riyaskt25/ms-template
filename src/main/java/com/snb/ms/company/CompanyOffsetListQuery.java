package com.snb.ms.company;

import com.snb.ms.shared.constants.ListQueryDefaults;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyOffsetListQuery {

    @PositiveOrZero
    private Integer page = ListQueryDefaults.DEFAULT_PAGE;

    @Positive
    @Max(ListQueryDefaults.MAX_PAGE_SIZE)
    private Integer size = ListQueryDefaults.DEFAULT_SIZE;

    private String sortBy;

    private String sortDirection = "ASC";

    private String registrationNumber;

    private String companyStatus;

    private String companyType;

    private String emailAddress;

    private String mobileNumber;

    @Schema(description = "Set true to include associated salesmen in each company response. Defaults to false.", example = "false", defaultValue = "false")
    private Boolean includeSalesmen = false;
}
