package com.sbs.apple;

import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ChatRoomService chatRoomService;
    @GetMapping("/")
    public String showMain(Model model) {
        ChatRoom chatRoom= chatRoomService.findLastRoom();
        if(chatRoom == null){
            chatRoom=chatRoomService.create(1);
        }

        model.addAttribute("chatRoom", chatRoom);
        return "main";
    }
}
