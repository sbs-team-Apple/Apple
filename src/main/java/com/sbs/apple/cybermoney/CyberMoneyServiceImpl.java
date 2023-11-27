package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class CyberMoneyServiceImpl implements CyberMoneyService {
    private final UserRepository userRepository;
    private final CyberMoneyTransactionRepository cyberMoneyTransactionRepository;

    public CyberMoneyServiceImpl(UserRepository siteUserRepository, CyberMoneyTransactionRepository cyberMoneyTransactionRepository) {
        this.userRepository = siteUserRepository;
        this.cyberMoneyTransactionRepository = cyberMoneyTransactionRepository;

    }
    public void JustSendCyberMoney(SiteUser senderUser, SiteUser recipientUser, int amount) {
        senderUser.setCyberMoney(senderUser.getCyberMoney() - amount);
        recipientUser.setReceivedCyberMoney(recipientUser.getReceivedCyberMoney() + amount);
        userRepository.save(senderUser);
        userRepository.save(recipientUser);
    }

    @Override
    public void sendCyberMoney(SiteUser senderUser, SiteUser recipientUser, int amount) {
        if (senderUser.getCyberMoney() >= amount) {
            // 먼저 거래 내역을 저장
            CyberMoneyTransaction transaction = new CyberMoneyTransaction();
            transaction.setSenderUser(senderUser);
            transaction.setRecipientUser(recipientUser);
            transaction.setAmount(amount);
            transaction.setTransactionDate(new Date());
            cyberMoneyTransactionRepository.save(transaction);

            // 그 후, 사용자들의 사이버 머니를 업데이트
            senderUser.setCyberMoney(senderUser.getCyberMoney() - amount);
//
            userRepository.save(senderUser);
//
        } else {
            throw new IllegalArgumentException("사이버 머니가 부족합니다.");
        }
    }

    @Override
        public CyberMoneyTransaction findByUserIdAndUserId2(Integer userId, Integer userId2) {

        if(cyberMoneyTransactionRepository.findByUserIdAndUserId2(userId, userId2)==null){
            return null;
        }
        return cyberMoneyTransactionRepository.findByUserIdAndUserId2(userId, userId2);

    }



}