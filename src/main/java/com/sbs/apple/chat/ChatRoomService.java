package com.sbs.apple.chat;

import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;


    public ChatRoom create(SiteUser user,SiteUser user2){
        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setUserId1(user);
//        chatRoom.setUserId2(userId2);
        chatRoom.setCreateDate(LocalDateTime.now());
        chatRoom.setSiteUser(user);
        chatRoom.setSiteUser2(user2);
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

    public List<ChatRoom> findByUser(SiteUser user){
       return chatRoomRepository.findChatRoomsByUserId(user);
    }




}
