package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;

public interface CyberMoneyService {
    void sendCyberMoney(SiteUser sender, SiteUser recipient, int amount);
}
