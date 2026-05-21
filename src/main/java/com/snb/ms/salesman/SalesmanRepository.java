package com.snb.ms.salesman;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman, Long> {

    @Query("SELECT s FROM Salesman s JOIN FETCH s.user WHERE s.deletedFlag = 'N' AND s.user.deletedFlag = 'N'")
    List<Salesman> findAllWithUser();

    @Query("SELECT s FROM Salesman s JOIN FETCH s.user WHERE s.salesmanId = :id AND s.deletedFlag = 'N' AND s.user.deletedFlag = 'N'")
    Optional<Salesman> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT s FROM Salesman s WHERE s.salesmanId = :id AND s.deletedFlag = 'N'")
    Optional<Salesman> findActiveById(@Param("id") Long id);

    @Query("SELECT s FROM Salesman s WHERE s.user.userId = :userId AND s.deletedFlag = 'N' AND s.user.deletedFlag = 'N'")
    Optional<Salesman> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM Salesman s WHERE s.accountNumber = :accountNumber AND s.deletedFlag = 'N'")
    Optional<Salesman> findByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT s FROM Salesman s WHERE s.cifNumber = :cifNumber AND s.deletedFlag = 'N'")
    Optional<Salesman> findByCifNumber(@Param("cifNumber") String cifNumber);

    @Query("SELECT s FROM Salesman s WHERE s.idNumber = :idNumber AND s.deletedFlag = 'N'")
    Optional<Salesman> findByIdNumber(@Param("idNumber") String idNumber);
}
