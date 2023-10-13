package com.sbs.apple.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;


    public ChatRoom create(Integer userId, Integer userId2){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserId1(userId);
        chatRoom.setUserId2(userId2);
        chatRoom.setCreateDate(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
        return chatRoom;

    }

    public Integer findLastId(){
        return chatRoomRepository.findMaxId();

    }

    public ChatRoom findLastRoom(){


        if(chatRoomRepository.findTopByOrderByIdDesc().isEmpty()){
            return null;
        }

        return chatRoomRepository.findTopByOrderByIdDesc().get();
    }

    public List<ChatRoom> findAll(){
        return chatRoomRepository.findAll();
    }

    public List<ChatRoom> findByUserId1(Integer userId1){
        return chatRoomRepository.findByUserId1(userId1);
    }




}
