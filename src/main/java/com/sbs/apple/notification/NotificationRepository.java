package com.sbs.apple.notification;


import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.user.SiteUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT cr FROM Notification cr WHERE cr.siteUser = :user AND cr.id = (SELECT MAX(n.id) FROM Notification n WHERE n.siteUser = :user)")
    Notification findLatestNotificationByUser(@Param("user") SiteUser user);

    @Query("SELECT cr FROM Notification cr WHERE cr.siteUser = :user ORDER BY cr.id DESC")
    List<Notification> findByUser(@Param("user") SiteUser user);

    @Query("SELECT cr FROM Notification cr WHERE cr.siteUserFrom = :user AND cr.siteUser = :user2")
    Notification findByUsers(@Param("user") SiteUser user,@Param("user2") SiteUser user2);
}
