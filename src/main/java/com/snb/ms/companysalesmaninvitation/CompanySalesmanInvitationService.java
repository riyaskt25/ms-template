package com.snb.ms.companysalesmaninvitation;

import com.snb.ms.company.Company;
import com.snb.ms.company.CompanyRepository;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
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
    private final CompanySalesmanInvitationRepository companySalesmanInvitationRepository;

    @Transactional
    public CompanySalesmanInvitationDto create(Long companyId, CompanySalesmanInvitationRequest request) {
        log.debug("Creating salesman invitation for companyId={} emailAddress={}", companyId, request.getEmailAddress());

        Company company = companyRepository.findActiveById(companyId)
            .orElseThrow(() -> ResourceNotFoundException.companyById(companyId));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = request.getExpiryDate() != null ? request.getExpiryDate() : now.plusDays(DEFAULT_EXPIRY_DAYS);

        boolean hasOpenInvitation = companySalesmanInvitationRepository.existsOpenInvitation(
            companyId,
            STATUS_INVITED,
            request.getEmailAddress(),
            request.getMobileNumber(),
            request.getIdNumber(),
            now
        );
        if (hasOpenInvitation) {
            throw BusinessValidationException.activeCompanySalesmanInvitationExists(
                companyId,
                request.getEmailAddress(),
                request.getMobileNumber(),
                request.getIdNumber()
            );
        }

        CompanySalesmanInvitation invitation = CompanySalesmanInvitation.builder()
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

        CompanySalesmanInvitation created = Objects.requireNonNull(companySalesmanInvitationRepository.save(invitation));
        log.info("Created salesman invitation id={} companyId={}", created.getCompanySalesmanInvitationId(), companyId);
        return toDto(created);
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
            invitation.getExpiryDate()
        );
    }
}