package com.snb.ms.quotation;

import com.snb.ms.auth.authorization.Privileges;
import com.snb.ms.auth.authorization.RequirePrivilege;
import com.snb.ms.company.Company;
import com.snb.ms.company.CompanyRepository;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.salesman.Salesman;
import com.snb.ms.salesman.SalesmanRepository;
import com.snb.ms.shared.request.RequestContextAccessor;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuotationService {

  private final QuotationRepository quotationRepository;
  private final QuotationMapper quotationMapper;
  private final CompanyRepository companyRepository;
  private final SalesmanRepository salesmanRepository;
  private final RequestContextAccessor contextAccessor;

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.QUOTATION_VIEW)
  public Page<QuotationResponse> findAll(QuotationListQuery query) {
    Page<Quotation> quotationsPage =
        quotationRepository.findAll(
            QuotationQueryBuilder.buildSpecification(),
            QuotationQueryBuilder.buildPageable(
                query.getPage(), query.getSize(), query.getSortBy(), query.getSortDirection()));
    return quotationsPage.map(quotationMapper::toDto);
  }

  @Transactional(readOnly = true)
  @RequirePrivilege(Privileges.QUOTATION_VIEW)
  public Optional<QuotationResponse> findById(Long id) {
    return quotationRepository.findActiveById(id).map(quotationMapper::toDto);
  }

  @Transactional
  @RequirePrivilege(Privileges.QUOTATION_MANAGE)
  public QuotationResponse create(QuotationCreateRequest request) {
    if (quotationRepository.existsActiveByQuotationNo(request.getQuotationNo())) {
      throw BusinessValidationException.quotationNoAlreadyExists(request.getQuotationNo());
    }

    Company company =
        companyRepository
            .findActiveById(request.getCompanyId())
            .orElseThrow(() -> ResourceNotFoundException.companyById(request.getCompanyId()));
    Salesman salesman =
        salesmanRepository
            .findActiveById(request.getSalesmanId())
            .orElseThrow(() -> ResourceNotFoundException.salesmanById(request.getSalesmanId()));

    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    Quotation quotation = quotationMapper.toEntity(request);
    quotation.setCompany(company);
    quotation.setSalesman(salesman);
    quotation.setCreatedAt(LocalDateTime.now());
    quotation.setCreatedBy(callerId);
    quotation.setDeletedFlag("N");
    quotation.setVersionNumber(0L);
    return quotationMapper.toDto(quotationRepository.save(quotation));
  }

  @Transactional
  @RequirePrivilege(Privileges.QUOTATION_MANAGE)
  public Optional<QuotationResponse> update(Long id, QuotationUpdateRequest request) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return quotationRepository
        .findActiveById(id)
        .map(
            existing -> {
              if (!existing.getQuotationNo().equals(request.getQuotationNo())
                  && quotationRepository.existsActiveByQuotationNo(request.getQuotationNo())) {
                throw BusinessValidationException.quotationNoAlreadyExists(
                    request.getQuotationNo());
              }

              Company company =
                  companyRepository
                      .findActiveById(request.getCompanyId())
                      .orElseThrow(
                          () -> ResourceNotFoundException.companyById(request.getCompanyId()));
              Salesman salesman =
                  salesmanRepository
                      .findActiveById(request.getSalesmanId())
                      .orElseThrow(
                          () -> ResourceNotFoundException.salesmanById(request.getSalesmanId()));

              quotationMapper.updateEntity(request, existing);
              existing.setCompany(company);
              existing.setSalesman(salesman);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return quotationMapper.toDto(quotationRepository.save(existing));
            });
  }

  @Transactional
  @RequirePrivilege(Privileges.QUOTATION_MANAGE)
  public Optional<QuotationResponse> softDelete(Long id) {
    Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
    LocalDateTime now = LocalDateTime.now();
    return quotationRepository
        .findActiveById(id)
        .map(
            existing -> {
              existing.setDeletedFlag("Y");
              existing.setDeletedAt(now);
              existing.setUpdatedAt(now);
              existing.setUpdatedBy(callerId);
              existing.setVersionNumber(existing.getVersionNumber() + 1);
              return quotationMapper.toDto(quotationRepository.save(existing));
            });
  }
}
