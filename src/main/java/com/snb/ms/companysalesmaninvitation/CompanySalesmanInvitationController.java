// File: src/main/java/com/snb/ms/companysalesmaninvitation/CompanySalesmanInvitationController.java
package com.snb.ms.companysalesmaninvitation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company-salesman-invitations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompanySalesmanInvitationController implements CompanySalesmanInvitationApi {

    private final CompanySalesmanInvitationService companySalesmanInvitationService;

    @Override
    @PostMapping
    public ResponseEntity<List<CompanySalesmanInvitationResponse>> create(
        @Valid @RequestBody CompanySalesmanInvitationRequest request
    ) {
        log.debug("Received request to create salesman invitations companyIds={} emailAddress={}", request.getCompanyIds(), request.getEmailAddress());
        List<CompanySalesmanInvitationResponse> created = companySalesmanInvitationService.create(request);
        log.info("Created {} salesman invitation(s) for companyIds={}", created.size(), request.getCompanyIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}