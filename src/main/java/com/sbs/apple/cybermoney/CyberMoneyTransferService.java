package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CyberMoneyTransferService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionLogRepository transactionLogRepository;

    public void transferCyberMoney(String senderUsername, String recipientUsername, int amount) {
        Optional<SiteUser> senderUser = userRepository.findByusername(senderUsername);
        Optional<SiteUser> recipientUser = userRepository.findByusername(recipientUsername);

        if (senderUser.isPresent() && recipientUser.isPresent()) {
            SiteUser sender = senderUser.get();
            SiteUser recipient = recipientUser.get();

            int senderCurrentCyberMoney = sender.getCyberMoney();

            if (senderCurrentCyberMoney >= amount) {
                int senderNewCyberMoney = senderCurrentCyberMoney - amount;
                sender.setCyberMoney(senderNewCyberMoney);

                int recipientCurrentCyberMoney = recipient.getCyberMoney();
                int recipientNewCyberMoney = recipientCurrentCyberMoney + amount;
                recipient.setCyberMoney(recipientNewCyberMoney);

                // 데이터베이스에 변경 내용 저장
                userRepository.save(sender);
                userRepository.save(recipient);

                // 거래 내역 기록
                TransactionLog senderLog = new TransactionLog(sender, recipient, amount);
                TransactionLog recipientLog = new TransactionLog(sender, recipient, amount);
                transactionLogRepository.save(senderLog);
                transactionLogRepository.save(recipientLog);

                // 사용자 엔티티에 거래 정보 추가
                sender.addSentTransaction(senderLog);
                recipient.addReceivedTransaction(recipientLog);
            } else {
                // 보내는 사용자의 사이버 머니 부족 시 예외 처리
                throw new InsufficientFundsException("사이버 머니가 부족합니다.");
            }
        } else {
            // 사용자가 존재하지 않을 때 예외 처리
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }
}
