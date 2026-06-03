package com.snb.ms.companysalesmaninvitation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class CompanySalesmanInvitationLookupController
    implements CompanySalesmanInvitationLookupApi {

  private final CompanySalesmanInvitationService companySalesmanInvitationService;

  @Override
  @GetMapping("/api/company-salesman-invitations")
  public List<CompanySalesmanInvitationListResponse> listByEmail(
      @NotBlank(message = "{validation.companySalesmanInvitation.lookupEmail.required}")
          @Email(message = "{validation.companySalesmanInvitation.lookupEmail.email}")
          @RequestParam(name = "email")
          String email) {
    log.debug("Received request to list salesman invitations emailAddress={}", email);
    return companySalesmanInvitationService.listByEmail(email);
  }
}
