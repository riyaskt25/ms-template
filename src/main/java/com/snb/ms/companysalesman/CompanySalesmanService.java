package com.snb.ms.companysalesman;

import com.snb.ms.companysalesman.CompanySalesmanDto;
import com.snb.ms.companysalesman.CompanySalesmanRequest;
import com.snb.ms.company.Company;
import com.snb.ms.companysalesman.CompanySalesman;
import com.snb.ms.salesman.Salesman;
import com.snb.ms.exception.ResourceNotFoundException;
import com.snb.ms.companysalesman.CompanySalesmanMapper;
import com.snb.ms.company.CompanyRepository;
import com.snb.ms.companysalesman.CompanySalesmanRepository;
import com.snb.ms.salesman.SalesmanRepository;
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
public class CompanySalesmanService {

    private final CompanySalesmanRepository companySalesmanRepository;
    private final CompanyRepository companyRepository;
    private final SalesmanRepository salesmanRepository;
    private final CompanySalesmanMapper companySalesmanMapper;

    @Transactional(readOnly = true)
    public List<CompanySalesmanDto> findAll() {
        log.debug("Fetching all company-salesman associations");
        List<CompanySalesmanDto> associations = companySalesmanMapper.toDtoList(companySalesmanRepository.findAll());
        log.info("Fetched {} company-salesman associations", associations.size());
        return associations;
    }

    @Transactional(readOnly = true)
    public Optional<CompanySalesmanDto> findById(Long id) {
        log.debug("Fetching company-salesman association by id={}", id);
        Optional<CompanySalesmanDto> result = companySalesmanRepository.findById(id).map(companySalesmanMapper::toDto);
        log.info("Company-salesman association lookup id={} found={}", id, result.isPresent());
        return result;
    }

    @Transactional(readOnly = true)
    public List<CompanySalesmanDto> findByCompanyId(Long companyId) {
        log.debug("Fetching company-salesman associations by companyId={}", companyId);
        List<CompanySalesmanDto> result = companySalesmanMapper.toDtoList(companySalesmanRepository.findByCompany_CompanyId(companyId));
        log.info("Fetched {} associations for companyId={}", result.size(), companyId);
        return result;
    }

    @Transactional(readOnly = true)
    public List<CompanySalesmanDto> findBySalesmanId(Long salesmanId) {
        log.debug("Fetching company-salesman associations by salesmanId={}", salesmanId);
        List<CompanySalesmanDto> result = companySalesmanMapper.toDtoList(companySalesmanRepository.findBySalesman_SalesmanId(salesmanId));
        log.info("Fetched {} associations for salesmanId={}", result.size(), salesmanId);
        return result;
    }

    @Transactional
    public void createAssociation(Long companyId, Salesman salesman) {
        log.debug("Creating company-salesman association companyId={} salesmanId={}", companyId, salesman.getSalesmanId());
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> {
                log.error("Company not found while creating association companyId={}", companyId);
                return new ResourceNotFoundException("Company not found: " + companyId);
            });

        CompanySalesman entity = CompanySalesman.builder()
            .company(company)
            .salesman(salesman)
            .associationStatus("ACTIVE")
            .joinedAt(LocalDateTime.now())
            .build();

        companySalesmanRepository.save(entity);
        log.info("Created company-salesman association companyId={} salesmanId={}", companyId, salesman.getSalesmanId());
    }

    @Transactional
    public CompanySalesmanDto create(Long companyId, Long salesmanId, CompanySalesmanRequest request) {
        log.debug("Creating company-salesman record companyId={} salesmanId={}", companyId, salesmanId);
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> {
                log.error("Company not found for company-salesman create companyId={}", companyId);
                return new ResourceNotFoundException("Company not found: " + companyId);
            });
        Salesman salesman = salesmanRepository.findById(salesmanId)
            .orElseThrow(() -> {
                log.error("Salesman not found for company-salesman create salesmanId={}", salesmanId);
                return new ResourceNotFoundException("Salesman not found: " + salesmanId);
            });
        CompanySalesman entity = companySalesmanMapper.toEntity(request);
        entity.setCompany(company);
        entity.setSalesman(salesman);
        CompanySalesmanDto created = companySalesmanMapper.toDto(companySalesmanRepository.save(entity));
        log.info("Created company-salesman record id={} companyId={} salesmanId={}", created.getCompanySalesmanId(), companyId, salesmanId);
        return created;
    }

    @Transactional
    public Optional<CompanySalesmanDto> update(Long id, CompanySalesmanRequest request) {
        log.debug("Updating company-salesman association id={}", id);
        Optional<CompanySalesmanDto> updated = companySalesmanRepository.findById(id).map(existing -> {
            companySalesmanMapper.updateEntity(request, existing);
            return companySalesmanMapper.toDto(companySalesmanRepository.save(existing));
        });
        log.info("Company-salesman update id={} success={}", id, updated.isPresent());
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Deleting company-salesman association id={}", id);
        companySalesmanRepository.deleteById(id);
        log.info("Deleted company-salesman association id={}", id);
    }
}
