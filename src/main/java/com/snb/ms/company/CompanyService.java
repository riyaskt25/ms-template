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
import com.snb.ms.shared.constants.ListQueryDefaults;
import com.snb.ms.companysalesman.CompanySalesmanRepository;
import com.snb.ms.companysalesman.CompanySalesman;
import com.snb.ms.shared.UserProvisioningService;
import com.snb.ms.shared.request.RequestContextAccessor;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    // Whitelist public sort fields to safe entity paths.
    private static final Map<String, String> ALLOWED_SORTS = Map.of(
        "companyId", "companyId",
        "registrationNumber", "registrationNumber",
        "companyStatus", "companyStatus",
        "companyType", "companyType",
        "emailAddress", "user.emailAddress",
        "mobileNumber", "user.mobileNumber",
        "createdAt", "createdAt",
        "updatedAt", "updatedAt"
    );

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserProvisioningService userProvisioningService;
    private final RequestContextAccessor contextAccessor;
    private final CompanySalesmanRepository companySalesmanRepository;

    @Transactional(readOnly = true)
    public Page<CompanyResponse> findAll(CompanyListQuery query) {
        Pageable pageable = buildPageable(query.getPage(), query.getSize(), query.getSortBy(), query.getSortDirection());
        Specification<Company> specification = buildSpecification(
            query.getRegistrationNumber(),
            query.getCompanyStatus(),
            query.getCompanyType(),
            query.getEmailAddress(),
            query.getMobileNumber()
        );

        Page<Company> companiesPage = companyRepository.findAll(specification, pageable);
        Page<CompanyResponse> companies = companiesPage.map(companyMapper::toDto);

        // Two-step loading: enrich only when includeSalesmen=true.
        if (Boolean.TRUE.equals(query.getIncludeSalesmen())) {
            enrichWithSalesmen(companies);
        }

        log.info("Fetched companies: page={}, size={}, returned={}, total={}",
            companies.getNumber(), companies.getSize(), companies.getNumberOfElements(), companies.getTotalElements());
        return companies;
    }

    private void enrichWithSalesmen(Page<CompanyResponse> companiesPage) {
        List<Long> companyIds = companiesPage.getContent().stream()
            .map(CompanyResponse::getCompanyId)
            .collect(Collectors.toList());

        if (companyIds.isEmpty()) {
            return;
        }

        // Bulk fetch salesmen for all companies in this page
        List<CompanySalesman> companySalesmen = companySalesmanRepository.findActiveByCompanyIds(companyIds);

        // Group by company ID for efficient mapping
        Map<Long, List<com.snb.ms.salesman.SalesmanResponse>> salesmenByCompanyId = new HashMap<>();
        for (CompanySalesman cs : companySalesmen) {
            Long companyId = cs.getCompany().getCompanyId();
            com.snb.ms.salesman.Salesman salesman = cs.getSalesman();
            com.snb.ms.salesman.SalesmanResponse salesmanResponse = new com.snb.ms.salesman.SalesmanResponse();
            salesmanResponse.setSalesmanId(salesman.getSalesmanId());
            salesmanResponse.setFirstName(salesman.getFirstName());
            salesmanResponse.setMiddleName(salesman.getMiddleName());
            salesmanResponse.setLastName(salesman.getLastName());
            salesmanResponse.setAccountNumber(salesman.getAccountNumber());
            salesmanResponse.setCifNumber(salesman.getCifNumber());
            salesmanResponse.setIdNumber(salesman.getIdNumber());
            salesmanResponse.setAvailableIncentiveAmount(salesman.getAvailableIncentiveAmount());
            salesmanResponse.setCreatedBy(salesman.getCreatedBy());
            salesmanResponse.setCreatedAt(salesman.getCreatedAt());
            salesmanResponse.setUpdatedBy(salesman.getUpdatedBy());
            salesmanResponse.setUpdatedAt(salesman.getUpdatedAt());
            salesmenByCompanyId.computeIfAbsent(companyId, k -> new ArrayList<>()).add(salesmanResponse);
        }

        // Attach salesmen to each company response
        for (CompanyResponse company : companiesPage.getContent()) {
            company.setSalesmen(salesmenByCompanyId.getOrDefault(company.getCompanyId(), List.of()));
        }
    }

    private Pageable buildPageable(Integer page, Integer size, String sortBy, String sortDirection) {
        int effectivePage = page == null ? ListQueryDefaults.DEFAULT_PAGE : Math.max(page, ListQueryDefaults.DEFAULT_PAGE);
        int requestedSize = size == null ? ListQueryDefaults.DEFAULT_SIZE : size;
        int effectiveSize = Math.min(Math.max(requestedSize, 1), ListQueryDefaults.MAX_PAGE_SIZE);

        Sort sort = buildSort(sortBy, sortDirection);
        return PageRequest.of(effectivePage, effectiveSize, sort);
    }

    private Sort buildSort(String sortBy, String sortDirection) {
        List<String> fields = splitCsv(sortBy);
        List<String> directions = splitCsv(sortDirection);
        boolean singleDirection = directions.size() == 1;

        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            String requestedField = fields.get(i);
            String mappedField = ALLOWED_SORTS.get(requestedField);
            if (mappedField == null) {
                continue;
            }

            String requestedDirection = directions.isEmpty()
                ? "ASC"
                : (singleDirection ? directions.get(0) : directions.get(Math.min(i, directions.size() - 1)));

            Sort.Direction direction = "DESC".equalsIgnoreCase(requestedDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
            orders.add(new Sort.Order(direction, mappedField));
        }

        if (orders.isEmpty()) {
            return Sort.by(Sort.Order.asc("companyId"));
        }
        return Sort.by(orders);
    }

    private List<String> splitCsv(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return List.of();
        }

        List<String> values = new ArrayList<>();
        for (String item : raw.split(",")) {
            String trimmed = item.trim();
            if (!trimmed.isEmpty()) {
                values.add(trimmed);
            }
        }
        return values;
    }

    private Specification<Company> buildSpecification(String registrationNumber,
                                                      String companyStatus,
                                                      String companyType,
                                                      String emailAddress,
                                                      String mobileNumber) {
        Specification<Company> specification = activeOnly();
        specification = specification.and(likeCompanyField("registrationNumber", registrationNumber));
        specification = specification.and(likeCompanyField("companyStatus", companyStatus));
        specification = specification.and(likeCompanyField("companyType", companyType));
        specification = specification.and(likeUserField("emailAddress", emailAddress));
        specification = specification.and(likeUserField("mobileNumber", mobileNumber));
        return specification;
    }

    private Specification<Company> activeOnly() {
        return (root, query, cb) -> cb.equal(root.get("deletedFlag"), "N");
    }

    private Specification<Company> likeCompanyField(String fieldName, String value) {
        if (isBlank(value)) {
            return null;
        }
        String normalized = normalizeLikeValue(value);
        return (root, query, cb) -> cb.like(cb.lower(root.get(fieldName)), normalized);
    }

    private Specification<Company> likeUserField(String fieldName, String value) {
        if (isBlank(value)) {
            return null;
        }
        String normalized = normalizeLikeValue(value);
        return (root, query, cb) -> {
            Join<Object, Object> userJoin = root.join("user", JoinType.INNER);
            return cb.like(cb.lower(userJoin.get(fieldName)), normalized);
        };
    }

    private String normalizeLikeValue(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
