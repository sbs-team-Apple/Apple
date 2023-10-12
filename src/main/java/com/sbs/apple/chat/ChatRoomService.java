package com.sbs.apple.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;


    public void create(Integer userId){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserId1(userId);
        chatRoom.setCreateDate(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);

    }

    public Integer findLastId(){
        return chatRoomRepository.findMaxId();

    }

    public ChatRoom findLastRoom(){

        ChatRoom chatRoom = chatRoomRepository.findTopByOrderByIdDesc().get();
        return chatRoom;
    }

    public List<ChatRoom> findAll(){
        return chatRoomRepository.findAll();
    }

    public List<ChatRoom> findByUserId1(Integer userId1){
        return chatRoomRepository.findByUserId1(userId1);
    }




}
