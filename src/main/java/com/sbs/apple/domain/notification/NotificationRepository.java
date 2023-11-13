package com.sbs.apple.domain.notification;


import com.sbs.apple.domain.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT cr FROM Notification cr WHERE cr.siteUser = :user")
    List<Notification> findByUser(@Param("user") SiteUser user);


    @Query("SELECT cr FROM Notification cr WHERE cr.siteUserFrom = :user AND cr.siteUser = :user2")
    Notification findByUsers(@Param("user") SiteUser user,@Param("user2") SiteUser user2);
}
