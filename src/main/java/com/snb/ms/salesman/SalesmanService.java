package com.snb.ms.salesman;

import com.snb.ms.salesman.SalesmanResponse;
import com.snb.ms.salesman.SalesmanCreateRequest;
import com.snb.ms.salesman.SalesmanUpdateRequest;
import com.snb.ms.shared.UsersRequest;
import com.snb.ms.salesman.Salesman;
import com.snb.ms.shared.Users;
import com.snb.ms.salesman.SalesmanMapper;
import com.snb.ms.salesman.SalesmanRepository;
import com.snb.ms.shared.UserProvisioningService;
import com.snb.ms.companysalesman.CompanySalesmanService;
import com.snb.ms.shared.request.RequestContextAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesmanService {

    private final SalesmanRepository salesmanRepository;
    private final SalesmanMapper salesmanMapper;
    private final UserProvisioningService userProvisioningService;
    private final CompanySalesmanService companySalesmanService;
    private final RequestContextAccessor contextAccessor;

    @Transactional(readOnly = true)
    public List<SalesmanResponse> findAll() {
        log.debug("Fetching all salesmen");
        List<SalesmanResponse> salesmen = salesmanMapper.toDtoList(salesmanRepository.findAllWithUser());
        log.info("Fetched {} salesmen", salesmen.size());
        return salesmen;
    }

    @Transactional(readOnly = true)
    public Optional<SalesmanResponse> findById(Long id) {
        log.debug("Fetching salesman by id={}", id);
        Optional<SalesmanResponse> result = salesmanRepository.findByIdWithUser(id).map(salesmanMapper::toDto);
        log.info("Salesman lookup id={} found={}", id, result.isPresent());
        return result;
    }

    @Transactional
    public SalesmanResponse create(SalesmanCreateRequest request) {
        log.debug("Creating salesman for companyId={}", request.getCompanyId());
        try {
            UsersRequest userRequest = new UsersRequest();
            userRequest.setEmailAddress(request.getEmailAddress());
            userRequest.setMobileNumber(request.getMobileNumber());
            userRequest.setUserType("SALESMAN");
            userRequest.setAccountStatus("ACTIVE");
            userRequest.setAccountLockedFlag("N");

            Users user = userProvisioningService.createUser(userRequest);
            Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
            Salesman salesman = salesmanMapper.toEntity(request);
            salesman.setUser(user);
            salesman.setCreatedAt(LocalDateTime.now());
            salesman.setCreatedBy(callerId);
            salesman.setDeletedFlag("N");
            salesman.setVersionNumber(0L);
            salesman.setAvailableIncentiveAmount(BigDecimal.ZERO);
            Salesman savedSalesman = salesmanRepository.save(salesman);
            companySalesmanService.createAssociation(request.getCompanyId(), savedSalesman);
            SalesmanResponse created = salesmanMapper.toDto(savedSalesman);
            log.info("Created salesman id={} for companyId={}", created.getSalesmanId(), request.getCompanyId());
            return created;
        } catch (RuntimeException ex) {
            log.error("Failed to create salesman for companyId={}", request.getCompanyId(), ex);
            throw ex;
        }
    }

    @Transactional
    public Optional<SalesmanResponse> update(Long id, SalesmanUpdateRequest request) {
        log.debug("Updating salesman id={}", id);
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        Optional<SalesmanResponse> updated = salesmanRepository.findById(id).map(existing -> {
            salesmanMapper.updateEntity(request, existing);
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setUpdatedBy(callerId);
            existing.setVersionNumber(existing.getVersionNumber() + 1);
            return salesmanMapper.toDto(salesmanRepository.save(existing));
        });
        log.info("Salesman update id={} success={}", id, updated.isPresent());
        return updated;
    }

    @Transactional
    public Optional<SalesmanResponse> softDelete(Long id) {
        log.debug("Soft-deleting salesman id={}", id);
        Long callerId = contextAccessor.currentUserIdAsLong().orElse(null);
        Optional<SalesmanResponse> deleted = salesmanRepository.findById(id).map(existing -> {
            existing.setDeletedFlag("Y");
            existing.setDeletedAt(LocalDateTime.now());
            existing.setUpdatedAt(LocalDateTime.now());
            existing.setUpdatedBy(callerId);
            return salesmanMapper.toDto(salesmanRepository.save(existing));
        });
        log.info("Salesman soft-delete id={} success={}", id, deleted.isPresent());
        return deleted;
    }
}
