package com.sbs.apple.cybermoney;

import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.user.SiteUser;

public interface CyberMoneyService {
    void sendCyberMoney(SiteUser senderUser, SiteUser recipientUser, int amount);

    CyberMoneyTransaction findByUserIdAndUserId2(Integer userId, Integer userId2);

//    public ChatRoom findRoomByUserIdAndUserId2(Integer userId, Integer userId2) {
//
//        if(chatRoomRepository.findRoomByUserIdAndUserId2(userId, userId2)==null){
//            return null;
//        }
//        return chatRoomRepository.findRoomByUserIdAndUserId2(userId, userId2);
//
//    }


}
