// File: src/main/java/com/snb/ms/companysalesmaninvitation/CompanySalesmanInvitationService.java
package com.snb.ms.companysalesmaninvitation;

import com.snb.ms.company.Company;
import com.snb.ms.company.CompanyMapper;
import com.snb.ms.company.CompanyRepository;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanySalesmanInvitationService {

  static final String STATUS_INVITED = "INVITED";
  private static final long DEFAULT_EXPIRY_DAYS = 7L;

  private final CompanyRepository companyRepository;
  private final CompanyMapper companyMapper;
  private final CompanySalesmanInvitationRepository companySalesmanInvitationRepository;

  @Transactional
  public List<CompanySalesmanInvitationResponse> create(CompanySalesmanInvitationRequest request) {
    log.debug(
        "Creating salesman invitations for companyIds={} emailAddress={}",
        request.getCompanyIds(),
        request.getEmailAddress());

    List<Long> companyIds = request.getCompanyIds().stream().distinct().toList();
    List<Company> companies = resolveCompanies(companyIds);

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expiryDate =
        request.getExpiryDate() != null
            ? request.getExpiryDate()
            : now.plusDays(DEFAULT_EXPIRY_DAYS);

    ensureNoOpenInvitations(companies, request, now);

    List<CompanySalesmanInvitationResponse> created = new ArrayList<>();
    for (Company company : companies) {
      CompanySalesmanInvitation invitation =
          CompanySalesmanInvitation.builder()
              .company(company)
              .salesmanId(null)
              .emailAddress(request.getEmailAddress())
              .mobileNumber(request.getMobileNumber())
              .idNumber(request.getIdNumber())
              .status(STATUS_INVITED)
              .invitedAt(now)
              .respondedAt(null)
              .expiryDate(expiryDate)
              .build();

      CompanySalesmanInvitation saved =
          Objects.requireNonNull(companySalesmanInvitationRepository.save(invitation));
      created.add(CompanySalesmanInvitationResponse.fromDto(toDto(saved)));
    }

    log.info("Created {} salesman invitation(s) for companyIds={}", created.size(), companyIds);
    return created;
  }

  @Transactional(readOnly = true)
  public List<CompanySalesmanInvitationListResponse> listByEmail(String emailAddress) {
    log.debug("Listing invitations for emailAddress={}", emailAddress);
    List<CompanySalesmanInvitation> invitations =
        companySalesmanInvitationRepository.findAllActiveByEmailAddress(emailAddress);
    List<CompanySalesmanInvitationListResponse> result =
        invitations.stream()
            .map(
                invitation ->
                    new CompanySalesmanInvitationListResponse(
                        invitation.getCompanySalesmanInvitationId(),
                        invitation.getSalesmanId(),
                        invitation.getEmailAddress(),
                        invitation.getMobileNumber(),
                        invitation.getIdNumber(),
                        invitation.getStatus(),
                        invitation.getInvitedAt(),
                        invitation.getRespondedAt(),
                        invitation.getExpiryDate(),
                        companyMapper.toDto(invitation.getCompany())))
            .toList();
    log.info("Found {} invitations for emailAddress={}", result.size(), emailAddress);
    return result;
  }

  private List<Company> resolveCompanies(List<Long> companyIds) {
    List<Company> companies = new ArrayList<>();
    for (Long companyId : companyIds) {
      Company company =
          companyRepository
              .findActiveById(companyId)
              .orElseThrow(() -> ResourceNotFoundException.companyById(companyId));
      companies.add(company);
    }
    return companies;
  }

  private void ensureNoOpenInvitations(
      List<Company> companies, CompanySalesmanInvitationRequest request, LocalDateTime now) {
    for (Company company : companies) {
      boolean hasOpenInvitation =
          companySalesmanInvitationRepository.existsOpenInvitation(
              company.getCompanyId(),
              STATUS_INVITED,
              request.getEmailAddress(),
              request.getMobileNumber(),
              request.getIdNumber(),
              now);
      if (hasOpenInvitation) {
        throw BusinessValidationException.activeCompanySalesmanInvitationExists(
            company.getCompanyId(),
            request.getEmailAddress(),
            request.getMobileNumber(),
            request.getIdNumber());
      }
    }
  }

  private CompanySalesmanInvitationDto toDto(CompanySalesmanInvitation invitation) {
    return new CompanySalesmanInvitationDto(
        invitation.getCompanySalesmanInvitationId(),
        invitation.getSalesmanId(),
        invitation.getEmailAddress(),
        invitation.getMobileNumber(),
        invitation.getIdNumber(),
        invitation.getCompany().getCompanyId(),
        invitation.getStatus(),
        invitation.getInvitedAt(),
        invitation.getRespondedAt(),
        invitation.getExpiryDate());
  }
}
