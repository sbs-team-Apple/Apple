package com.sbs.apple.domain.chat;

import com.sbs.apple.domain.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    @Query(value = "SELECT MAX(id) FROM your_entity", nativeQuery = true)
    Integer findMaxId();

    Optional<ChatRoom> findTopByOrderByIdDesc();

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.siteUser = :user OR cr.siteUser2 = :user")
    List<ChatRoom> findChatRoomsByUserId(@Param("user") SiteUser user);



    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.siteUser.id = :userId AND cr.siteUser2.id = :userId2) OR (cr.siteUser.id = :userId2 AND cr.siteUser2.id = :userId)")
    ChatRoom findRoomByUserIdAndUserId2(@Param("userId") Integer userId,@Param("userId2") Integer userId2);






}
