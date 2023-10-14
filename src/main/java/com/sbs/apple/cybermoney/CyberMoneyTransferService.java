package com.sbs.apple.cybermoney;

import com.sbs.apple.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CyberMoneyTransferService {
    @Autowired
    private UserRepository userRepository;

    public void transferCyberMoney(String senderUsername, String recipientUsername, int amount) {
        // 사용자의 사이버 머니 전송 로직을 구현
        // 보유한 사이버 머니에서 amount 만큼 차감하고 수신자에게 더합니다.
    }
}
