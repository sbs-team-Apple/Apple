package com.sbs.apple;

import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.chat.ChatRoomService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    @GetMapping("/")
    public String showMain(Model model) {
        ChatRoom chatRoom= chatRoomService.findLastRoom();
        if(chatRoom == null){
            chatRoom=chatRoomService.create(1,1);
        }

        List<SiteUser> siteUsers = userService.getAllUser();


        model.addAttribute("siteUsers", siteUsers);

        model.addAttribute("chatRoom", chatRoom);
        return "main";
    }
}
