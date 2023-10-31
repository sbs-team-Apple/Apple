package com.sbs.apple.notification;


import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT cr FROM Notification cr WHERE cr.siteUser = :user")
    List<Notification> findByUser(@Param("user") SiteUser user);
}
