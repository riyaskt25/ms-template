package com.snb.ms.salesman;

import com.snb.ms.salesman.Salesman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman, Long> {

    @Query("SELECT s FROM Salesman s JOIN FETCH s.user")
    List<Salesman> findAllWithUser();

    @Query("SELECT s FROM Salesman s JOIN FETCH s.user WHERE s.salesmanId = :id")
    Optional<Salesman> findByIdWithUser(@Param("id") Long id);

    Optional<Salesman> findByUser_UserId(Long userId);

    Optional<Salesman> findByAccountNumber(String accountNumber);

    Optional<Salesman> findByCifNumber(String cifNumber);

    Optional<Salesman> findByIdNumber(String idNumber);
}
