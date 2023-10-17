package com.sbs.apple;

import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.chat.ChatRoomService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    @GetMapping("/")
    public String showMain(Model model, Principal principal) {
        System.out.println("메인페이지 실행1");
        ChatRoom chatRoom= chatRoomService.findLastRoom();

        if(principal==null){
            return "main";
        }
        SiteUser loginUser = userService.getUserbyName(principal.getName());
        List<SiteUser> siteUsers2 = userService.getAllUser2(loginUser);

        List<SiteUser> siteUsers =userService.getUsersNotRoom(loginUser,siteUsers2);

//
//        //나를 제외한 사이트 유저들 목록
//        List<SiteUser> siteUsers2= userService.getAllUser2(loginUser);
//
//
//        List<ChatRoom> AllRooms = chatRoomService.getAll();
//        //이미 채팅방이 있는 유저들 id 저장소
//        List<Integer> chatUsers= new ArrayList<>();
//
//        for(int i=0;i<AllRooms.size(); i++){
//            if(AllRooms.get(i).getSiteUser().getId()==loginUser.getId()){
//                chatUsers.add(AllRooms.get(i).getSiteUser2().getId()); //내가 초대한 유저의 아이디 저장
//            }
//
//            if(AllRooms.get(i).getSiteUser2().getId()==loginUser.getId()){
//                chatUsers.add(AllRooms.get(i).getSiteUser().getId()); //나를 초대한 유저의 아이디 저장
//            }
//        }
//
//        //채팅방이 이미 있어서 삭제해야할  유저들이 있어야되는 chatUsers 에서의 인덱스값 들
//        List<Integer> deleteUserIds= new ArrayList<>();
//        System.out.println(chatUsers.toString());
//
//        //그 인덱스 값들에 있는 user 들 삭제해서 중복된 채팅방 못만들게 하기
//        for(int i=0; i<siteUsers2.size();i++){
//            for (int j = 0; j < chatUsers.size(); j++) {
//                if(siteUsers2.get(i).getId()==chatUsers.get(j)){
//                    deleteUserIds.add(i);
//
//                }
//            }
//
//
//        }
//
//
//        for (int i = deleteUserIds.size() - 1; i >= 0; i--) {
//            siteUsers2.remove((int) deleteUserIds.get(i));
//        }
//


        System.out.println("메인페이지 실행2");
        model.addAttribute("siteUsers", siteUsers);

        model.addAttribute("chatRoom", chatRoom);

        //ChatRoom chatRoom= chatRoomService.findLastRoom();
        //if(chatRoom == null){
        //    chatRoom=chatRoomService.create(1);
        //}
        System.out.println("메인페이지 실행3");
        //model.addAttribute("chatRoom", chatRoom);
        List<SiteUser> userList = userService.getFourUsers(); // 사용자 정보를 가져오는 예시 메서드
        model.addAttribute("userList", userList);

        return "main";
    }
}
