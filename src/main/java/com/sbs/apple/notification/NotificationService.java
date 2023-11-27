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


    public Notification create(SiteUser siteUserTo, SiteUser siteUserFrom, String chatRoom){


            Notification notification = new Notification();

        notification.setCreateDate(LocalDateTime.now());
        notification.setSiteUser(siteUserTo);
        notification.setSiteUserFrom(siteUserFrom);
        notification.setKind(chatRoom);
        notificationRepository.save(notification);
            return notification;





    }


    public List<Notification> getByUserTo(SiteUser loginUser) {
        if(notificationRepository.findByUser(loginUser)==null){
            return null;
        }
        return notificationRepository.findByUser(loginUser);
    }

    public Notification findByUsers(SiteUser siteUser, SiteUser siteUser2) {

        if(notificationRepository.findByUsers(siteUser,siteUser2)==null){
            return null;

        };

        return notificationRepository.findByUsers(siteUser,siteUser2);


    }

    public void delete(Notification notification){
        notificationRepository.delete(notification);
    }

    public Notification findById(Integer id){

        return notificationRepository.findById(id).get();
    }
}
