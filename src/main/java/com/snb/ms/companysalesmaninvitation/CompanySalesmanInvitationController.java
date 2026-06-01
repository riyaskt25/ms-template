package com.snb.ms.companysalesmaninvitation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies/{companyId}/salesman-invitations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompanySalesmanInvitationController implements CompanySalesmanInvitationApi {

    private final CompanySalesmanInvitationService companySalesmanInvitationService;

    @Override
    @PostMapping
    public ResponseEntity<CompanySalesmanInvitationResponse> create(
        @Positive(message = "{validation.common.id.positive}") @PathVariable Long companyId,
        @Valid @RequestBody CompanySalesmanInvitationRequest request
    ) {
        log.debug("Received request to create salesman invitation companyId={} emailAddress={}", companyId, request.getEmailAddress());
        CompanySalesmanInvitationDto created = companySalesmanInvitationService.create(companyId, request);
        log.info("Created salesman invitation id={} companyId={}", created.getCompanySalesmanInvitationId(), companyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CompanySalesmanInvitationResponse.fromDto(created));
    }
}