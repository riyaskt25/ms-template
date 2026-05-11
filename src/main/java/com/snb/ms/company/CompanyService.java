package com.snb.ms.company;

import com.snb.ms.company.CompanyResponse;
import com.snb.ms.company.CompanyCreateRequest;
import com.snb.ms.company.CompanyUpdateRequest;
import com.snb.ms.shared.UsersRequest;
import com.snb.ms.company.Company;
import com.snb.ms.shared.Users;
import com.snb.ms.company.CompanyMapper;
import com.snb.ms.company.CompanyRepository;
import com.snb.ms.shared.UserProvisioningService;
import com.snb.ms.shared.request.RequestContextAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final UserProvisioningService userProvisioningService;
    private final RequestContextAccessor contextAccessor;

    @Transactional(readOnly = true)
    public List<CompanyResponse> findAll() {
        log.debug("Fetching all companies");
        List<CompanyResponse> companies = companyMapper.toDtoList(companyRepository.findAllActiveWithUser());
        log.info("Fetched {} companies", companies.size());
        return companies;
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
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        Company company = companyMapper.toEntity(request);
        company.setUser(user);
        company.setCreatedAt(LocalDateTime.now());
        company.setCreatedBy(callerId);
        company.setCompanyStatus("PENDING");
        company.setDeletedFlag("N");
        company.setVersionNumber(0L);
        CompanyResponse created = companyMapper.toDto(companyRepository.save(company));
        log.info("Created company id={} registrationNumber={}", created.getCompanyId(), request.getRegistrationNumber());
        return created;
    }

    @Transactional
    public Optional<CompanyResponse> update(Long id, CompanyUpdateRequest request) {
        log.debug("Updating company id={}", id);
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        Optional<CompanyResponse> updated = companyRepository.findActiveById(id).map(existing -> {
            companyMapper.updateEntity(request, existing);
            Users user = existing.getUser();
            if (user != null) {
                user.setEmailAddress(request.getEmailAddress());
                user.setMobileNumber(request.getMobileNumber());
            }
            existing.setUpdatedAt(LocalDateTime.now());
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
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        Optional<CompanyResponse> deleted = companyRepository.findActiveById(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(LocalDateTime.now());
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setUpdatedBy(callerId);
            return companyMapper.toDto(companyRepository.save(existing));
        });
        log.info("Company soft-delete id={} success={}", id, deleted.isPresent());
        return deleted;
    }
}
