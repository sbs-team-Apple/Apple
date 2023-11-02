package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CyberMoneyTransactionRepository extends JpaRepository<CyberMoneyTransaction, Long> {
    List<CyberMoneyTransaction> findByRecipientUser(SiteUser recipientUser);

    List<CyberMoneyTransaction> findBySenderUser(SiteUser user);
}

