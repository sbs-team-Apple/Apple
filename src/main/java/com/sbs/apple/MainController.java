package com.sbs.apple;

import com.sbs.apple.chat.ChatRoomService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import com.sbs.apple.util.DataCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ChatRoomService chatRoomService;
    private final UserService userService;

    private final DataCreator dataCreator;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAutoValue;

    // ************ TEST DATA CREATE ***************
    private boolean isTestDataCreated = false;
    // *********************************************

    @GetMapping("/")
    public String showMain(Model model) {
        //ChatRoom chatRoom= chatRoomService.findLastRoom();
        //if(chatRoom == null){
        //    chatRoom=chatRoomService.create(1);
        //}

        //model.addAttribute("chatRoom", chatRoom);

        // ************ TEST DATA CREATE ***************
        if (!isTestDataCreated && ddlAutoValue.equals("create")) {
            dataCreator.createTestData();
            isTestDataCreated = true;
        }// *********************************************

        List<SiteUser> userList = userService.getFourUsers(); // 사용자 정보를 가져오는 예시 메서드
        model.addAttribute("userList", userList);
        return "main";
    }
}
