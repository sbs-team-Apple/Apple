package com.sbs.apple.notification;


import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;


    public Notification create(SiteUser siteUserTo, SiteUser siteUserFrom){


            Notification notification = new Notification();

        notification.setCreateDate(LocalDateTime.now());
        notification.setSiteUser(siteUserTo);
        notification.setSiteUserFrom(siteUserFrom);
        notificationRepository.save(notification);
            return notification;





    }


    public List<Notification> getByUserTo(SiteUser loginUser) {
        if(notificationRepository.findByUser(loginUser)==null){
            return null;
        }
        return notificationRepository.findByUser(loginUser);
    }
}
