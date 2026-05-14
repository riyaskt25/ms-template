package com.snb.ms.company;

import com.snb.ms.shared.request.BaseListQuery;
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

    private Boolean includeSalesmen;
}
