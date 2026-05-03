package com.snb.ms.salesman;

import com.snb.ms.salesman.Salesman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman, Long> {

    Optional<Salesman> findByUser_UserId(Long userId);

    Optional<Salesman> findByAccountNumber(String accountNumber);

    Optional<Salesman> findByCifNumber(String cifNumber);

    Optional<Salesman> findByIdNumber(String idNumber);
}
