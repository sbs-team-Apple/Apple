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
        if(chatRoom == null){



        }
        if(principal==null){
            return "main";
        }
        SiteUser loginUser = userService.getUserbyName(principal.getName());


        List<SiteUser> siteUsers = userService.getAllUser();
        System.out.println("현재 유저수 "+siteUsers.size());


        List<ChatRoom> AllRooms = chatRoomService.getAll();
        //이미 채팅방이 있는 유저들 id 저장소
        List<Integer> chatUsers= new ArrayList<>();

        for(int i=0;i<AllRooms.size(); i++){
            if(AllRooms.get(i).getSiteUser().getId()==loginUser.getId()){
                chatUsers.add(AllRooms.get(i).getSiteUser2().getId()); //내가 초대한 유저의 아이디 저장
            }

            if(AllRooms.get(i).getSiteUser2().getId()==loginUser.getId()){
                chatUsers.add(AllRooms.get(i).getSiteUser().getId()); //나를 초대한 유저의 아이디 저장
            }
        }

        System.out.println(chatUsers.toString());

        for(int i=0; i<chatUsers.size();i++){
            siteUsers.remove(chatUsers.get(i)-1);
        }

//        for(int i=0; i< siteUsers.size(); i++){
//
//           for(int j=0; j< siteUsers.get(i).getChatRoomList().size(); j++) {
//               System.out.println("asdasdasdasd");
//               System.out.println("초대한 사람 id 명단"+siteUsers.get(i).getChatRoomList().get(j).getSiteUser().getId());
//               if (siteUsers.get(i).getChatRoomList().get(j).getSiteUser().getId() == loginUser.getId()) {
//                   System.out.println("내가 초대한적 있는 사람");
//                   System.out.println(j);
//                   c.add(i);
//               }
//               if (siteUsers.get(i).getChatRoomList().get(j).getSiteUser2().getId() == loginUser.getId()) {
//                   System.out.println("나한테 초대한적 있는 사람");
//                   System.out.println(j);
//                   c.add(i);
//               }
//           }
//
//
//           }
//        System.out.println(c.toString());





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
