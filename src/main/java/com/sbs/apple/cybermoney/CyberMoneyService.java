package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;

public interface CyberMoneyService {
    void sendCyberMoney(SiteUser senderUser, SiteUser recipientUser, int amount);
    CyberMoneyTransaction findByUserIdAndUserId2(Integer userId, Integer userId2);


}
