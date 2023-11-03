package com.sbs.apple.user;

import com.sbs.apple.cybermoney.CyberMoneyTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTransactionService {
    private UserRepository userRepository;

    @Autowired
    public UserTransactionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void moveTransactionToCompleted(SiteUser recipientUser, CyberMoneyTransaction transaction) {
        // 아래의 코드로 거래를 receivedTransactions에서 삭제하고 completedTransactions로 이동
        recipientUser.getReceivedTransactions().remove(transaction);
        recipientUser.getCompletedTransactions().add(transaction);
        userRepository.save(recipientUser);
    }
}
