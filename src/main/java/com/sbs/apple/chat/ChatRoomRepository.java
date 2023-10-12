package com.sbs.apple.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    @Query(value = "SELECT MAX(id) FROM your_entity", nativeQuery = true)
    Integer findMaxId();

    Optional<ChatRoom> findTopByOrderByIdDesc();
}
