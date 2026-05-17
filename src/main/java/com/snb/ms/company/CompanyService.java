package com.snb.ms.company;

import com.snb.ms.company.CompanyResponse;
import com.snb.ms.company.CompanyCreateRequest;
import com.snb.ms.company.CompanyStatus;
import com.snb.ms.company.CompanyStatusDecisionRequest;
import com.snb.ms.company.CompanyUpdateRequest;
import com.snb.ms.exception.BusinessValidationException;
import com.snb.ms.shared.UsersRequest;
import com.snb.ms.company.Company;
import com.snb.ms.shared.Users;
import com.snb.ms.company.CompanyMapper;
import com.snb.ms.company.CompanyRepository;
import com.snb.ms.companysalesman.CompanySalesmanRepository;
import com.snb.ms.companysalesman.CompanySalesman;
import com.snb.ms.salesman.SalesmanMapper;
import com.snb.ms.shared.UserProvisioningService;
import com.snb.ms.shared.request.RequestContextAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final SalesmanMapper salesmanMapper;
    private final UserProvisioningService userProvisioningService;
    private final RequestContextAccessor contextAccessor;
    private final CompanySalesmanRepository companySalesmanRepository;

    @Transactional(readOnly = true)
    public Page<CompanyResponse> findAll(CompanyListQuery query) {
        Page<Company> companiesPage = companyRepository.findAll(
            CompanyQueryBuilder.buildSpecification(
                query.getRegistrationNumber(),
                query.getCompanyStatus(),
                query.getCompanyType(),
                query.getEmailAddress(),
                query.getMobileNumber()
            ),
            CompanyQueryBuilder.buildPageable(
                query.getPage(),
                query.getSize(),
                query.getSortBy(),
                query.getSortDirection()
            )
        );

        Page<CompanyResponse> companies = companiesPage.map(companyMapper::toDto);

        // Two-step loading: enrich only when includeSalesmen=true.
        if (Boolean.TRUE.equals(query.getIncludeSalesmen())) {
            enrichWithSalesmen(companies);
        }

        log.info("Fetched companies: page={}, size={}, returned={}, total={}",
            companies.getNumber(), companies.getSize(), companies.getNumberOfElements(), companies.getTotalElements());
        return companies;
    }

    @Transactional(readOnly = true)
    public CompanyCursorSlice findAllLazy(CompanyListQuery query) {
        Specification<Company> spec = CompanyQueryBuilder.withCursor(
            CompanyQueryBuilder.buildSpecification(
                query.getRegistrationNumber(),
                query.getCompanyStatus(),
                query.getCompanyType(),
                query.getEmailAddress(),
                query.getMobileNumber()
            ),
            query.getCursor(),
            query.getSortBy(),
            query.getSortDirection()
        );

        Pageable pageable = CompanyQueryBuilder.buildCursorPageable(
            query.getLimit(),
            query.getSortBy(),
            query.getSortDirection()
        );
        Page<Company> companiesPage = companyRepository.findAll(spec, pageable);

        int limit = query.getLimit() == null ? pageable.getPageSize() - 1 : query.getLimit();
        List<Company> content = companiesPage.getContent();
        boolean hasNext = content.size() > limit;
        if (hasNext) {
            content = content.subList(0, limit);
        }

        List<CompanyResponse> data = content.stream().map(companyMapper::toDto).collect(Collectors.toList());
        if (Boolean.TRUE.equals(query.getIncludeSalesmen())) {
            enrichWithSalesmenList(data);
        }

        String nextCursor = hasNext && !content.isEmpty()
            ? CompanyQueryBuilder.buildNextCursor(content.get(content.size() - 1), query.getSortBy(), query.getSortDirection())
            : null;

        return new CompanyCursorSlice(data, limit, hasNext, nextCursor);
    }

    private void enrichWithSalesmen(Page<CompanyResponse> companiesPage) {
        enrichWithSalesmenList(companiesPage.getContent());
    }

    private void enrichWithSalesmenList(List<CompanyResponse> companies) {
        List<Long> companyIds = companies.stream()
            .map(CompanyResponse::getCompanyId)
            .collect(Collectors.toList());

        if (companyIds.isEmpty()) {
            return;
        }

        // Bulk fetch salesmen for all companies in this page
        List<CompanySalesman> companySalesmen = companySalesmanRepository.findActiveByCompanyIds(companyIds);

        // Group by company ID and map salesmen to responses using mapper
        Map<Long, List<com.snb.ms.salesman.SalesmanResponse>> salesmenByCompanyId = companySalesmen.stream()
            .collect(Collectors.groupingBy(
                cs -> cs.getCompany().getCompanyId(),
                Collectors.mapping(cs -> salesmanMapper.toDto(cs.getSalesman()), Collectors.toList())
            ));

        // Attach salesmen to each company response
        for (CompanyResponse company : companies) {
            company.setSalesmen(salesmenByCompanyId.getOrDefault(company.getCompanyId(), List.of()));
        }
    }

    public static class CompanyCursorSlice {
        private final List<CompanyResponse> data;
        private final Integer limit;
        private final boolean hasNext;
        private final String nextCursor;

        public CompanyCursorSlice(List<CompanyResponse> data, Integer limit, boolean hasNext, String nextCursor) {
            this.data = data;
            this.limit = limit;
            this.hasNext = hasNext;
            this.nextCursor = nextCursor;
        }

        public List<CompanyResponse> getData() {
            return data;
        }

        public Integer getLimit() {
            return limit;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public String getNextCursor() {
            return nextCursor;
        }
    }



    @Transactional(readOnly = true)
    public Optional<CompanyResponse> findById(Long id) {
        log.debug("Fetching company by id={}", id);
        Optional<CompanyResponse> result = companyRepository.findActiveById(id).map(companyMapper::toDto);
        log.info("Company lookup id={} found={}", id, result.isPresent());
        return result;
    }

    @Transactional
    public CompanyResponse create(CompanyCreateRequest request) {
        log.debug("Creating company for registrationNumber={}", request.getRegistrationNumber());
        UsersRequest userRequest = new UsersRequest();
        userRequest.setEmailAddress(request.getEmailAddress());
        userRequest.setMobileNumber(request.getMobileNumber());
        userRequest.setUserType("COMPANY");
        userRequest.setAccountStatus("ACTIVE");
        userRequest.setAccountLockedFlag("N");

        Users user = userProvisioningService.createUser(userRequest);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        Company company = companyMapper.toEntity(request);
        company.setUser(user);
        company.setCreatedAt(LocalDateTime.now());
        company.setCreatedBy(callerId);
        company.setCompanyStatus(CompanyStatus.PENDING.name());
        company.setCompanyType("DEALER");
        company.setDeletedFlag("N");
        company.setVersionNumber(0L);
        CompanyResponse created = companyMapper.toDto(companyRepository.save(company));
        log.info("Created company id={} registrationNumber={}", created.getCompanyId(), request.getRegistrationNumber());
        return created;
    }

    @Transactional
    public Optional<CompanyResponse> update(Long id, CompanyUpdateRequest request) {
        log.debug("Updating company id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<CompanyResponse> updated = companyRepository.findActiveById(id).map(existing -> {
            companyMapper.updateEntity(request, existing);
            Users user = existing.getUser();
            if (user != null) {
                user.setEmailAddress(request.getEmailAddress());
                user.setMobileNumber(request.getMobileNumber());
                user.setUpdatedAt(now);
                user.setUpdatedBy(callerId);
                user.setVersionNumber((user.getVersionNumber() == null ? 0L : user.getVersionNumber()) + 1);
            }
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return companyMapper.toDto(companyRepository.save(existing));
        });
        log.info("Company update id={} success={}", id, updated.isPresent());
        return updated;
    }

    @Transactional
    public Optional<CompanyResponse> softDelete(Long id) {
        log.debug("Soft-deleting company id={}", id);
        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<CompanyResponse> deleted = companyRepository.findActiveById(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(now);
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return companyMapper.toDto(companyRepository.save(existing));
        });
        log.info("Company soft-delete id={} success={}", id, deleted.isPresent());
        return deleted;
    }

    @Transactional
    public Optional<CompanyResponse> decideStatus(Long id, CompanyStatusDecisionRequest request) {
        log.debug("Applying status decision for company id={} targetStatus={}", id, request.getStatus());
        CompanyStatus targetStatus = CompanyStatus.fromValue(request.getStatus())
            .orElseThrow(() -> BusinessValidationException.invalidCompanyStatusValue(request.getStatus()));

        if (targetStatus == CompanyStatus.PENDING) {
            throw BusinessValidationException.invalidCompanyStatusValue(request.getStatus());
        }

        Long callerId = contextAccessor.headerUserIdAsLong().orElse(null);
        LocalDateTime now = LocalDateTime.now();
        Optional<CompanyResponse> decided = companyRepository.findActiveById(id).map(existing -> {
            CompanyStatus currentStatus = CompanyStatus.fromValue(existing.getCompanyStatus())
                .orElseThrow(() -> BusinessValidationException.invalidCompanyStatusTransition(existing.getCompanyStatus(), targetStatus.name()));

            if (currentStatus != CompanyStatus.PENDING) {
                throw BusinessValidationException.invalidCompanyStatusTransition(currentStatus.name(), targetStatus.name());
            }

            existing.setCompanyStatus(targetStatus.name());
            Users user = existing.getUser();
            if (user != null) {
                if (targetStatus == CompanyStatus.ACTIVE) {
                    user.setAccountStatus(CompanyStatus.ACTIVE.name());
                    user.setAccountLockedFlag("N");
                } else {
                    user.setAccountStatus(CompanyStatus.REJECTED.name());
                    user.setAccountLockedFlag("Y");
                }
                user.setUpdatedAt(now);
                user.setUpdatedBy(callerId);
                user.setVersionNumber((user.getVersionNumber() == null ? 0L : user.getVersionNumber()) + 1);
            }
            existing.setUpdatedAt(now);
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return companyMapper.toDto(companyRepository.save(existing));
        });

        log.info("Company status decision id={} success={}", id, decided.isPresent());
        return decided;
    }
}
