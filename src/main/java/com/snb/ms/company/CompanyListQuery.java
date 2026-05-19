package com.snb.ms.company;

import com.snb.ms.shared.request.BaseListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyListQuery extends BaseListQuery {

    private String registrationNumber;

    private String companyStatus;

    private String companyType;

    private String emailAddress;

    private String mobileNumber;

    @Schema(description = "Set true to include associated salesmen in each company response. Defaults to false.", example = "false", defaultValue = "false")
    private Boolean includeSalesmen = false;
}
