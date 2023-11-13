package com.sbs.apple.domain.cybermoney;

import com.sbs.apple.domain.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CyberMoneyTransactionRepository extends JpaRepository<CyberMoneyTransaction, Long> {
    List<CyberMoneyTransaction> findByRecipientUser(SiteUser recipientUser);

    List<CyberMoneyTransaction> findBySenderUser(SiteUser user);


    @Query("SELECT cr FROM CyberMoneyTransaction cr WHERE ((cr.senderUser.id = :userId AND cr.recipientUser.id = :userId2) OR (cr.senderUser.id = :userId2 AND cr.recipientUser.id = :userId)) AND cr.rejected = false AND cr.accepted = false")

    CyberMoneyTransaction findByUserIdAndUserId2(@Param("userId") Integer userId, @Param("userId2") Integer userId2);


}

