package com.snb.ms.companysalesmaninvitation;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanySalesmanInvitationRepository
    extends JpaRepository<CompanySalesmanInvitation, Long> {

  @Query(
      """
        SELECT CASE WHEN COUNT(invitation) > 0 THEN true ELSE false END
        FROM CompanySalesmanInvitation invitation
        WHERE invitation.company.companyId = :companyId
          AND invitation.status = :status
          AND invitation.respondedAt IS NULL
          AND invitation.expiryDate >= :now
          AND (
              invitation.emailAddress = :emailAddress
              OR invitation.mobileNumber = :mobileNumber
              OR invitation.idNumber = :idNumber
          )
        """)
  boolean existsOpenInvitation(
      @Param("companyId") Long companyId,
      @Param("status") String status,
      @Param("emailAddress") String emailAddress,
      @Param("mobileNumber") String mobileNumber,
      @Param("idNumber") String idNumber,
      @Param("now") LocalDateTime now);

  @Query(
      """
        SELECT invitation
        FROM CompanySalesmanInvitation invitation
        JOIN FETCH invitation.company company
        WHERE LOWER(invitation.emailAddress) = LOWER(:emailAddress)
          AND company.deletedFlag = 'N'
        ORDER BY invitation.invitedAt DESC, invitation.companySalesmanInvitationId DESC
        """)
  List<CompanySalesmanInvitation> findAllActiveByEmailAddress(
      @Param("emailAddress") String emailAddress);
}
