package com.sbs.apple;

import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.chat.ChatRoomService;
import com.sbs.apple.notification.Notification;
import com.sbs.apple.notification.NotificationService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserRepository;
import com.sbs.apple.user.UserService;
import com.sbs.apple.util.DataCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final DataCreator dataCreator;
    private final NotificationService notificationService;
    private final UserRepository userRepository;


    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAutoValue;

    // ************ TEST DATA CREATE ***************
    private boolean isTestDataCreated = false;
    // *********************************************

    @GetMapping("/")
    public String showMain(Model model, Principal principal) {
        System.out.println("메인페이지 실행1");

        // ************ TEST DATA CREATE ***************
        if (!isTestDataCreated && ddlAutoValue.equals("create") && userRepository.count() == 0) {
            dataCreator.createTestData();
            isTestDataCreated = true;

        }// *********************************************

        ChatRoom chatRoom= chatRoomService.findLastRoom();

        if(principal==null){
            return "th/main";
        }

        SiteUser loginUser = userService.getUserbyName(principal.getName());

        if(loginUser==null) {
            return "th/main";
        }

        List<SiteUser> siteUsers2 = userService.getAllUser2(loginUser);

        List<SiteUser> siteUsers =userService.getUsersNotRoom(loginUser,siteUsers2);



        System.out.println("메인페이지 실행2");
        model.addAttribute("siteUsers", siteUsers);
        model.addAttribute("chatRoom", chatRoom);

        System.out.println("메인페이지 실행3");

        SiteUser closeSiteUser = userService.getUser(1);
        model.addAttribute("closeSiteUser", closeSiteUser);
        String username = principal.getName();
        SiteUser siteUser =userService.getUserbyName(username);
        boolean userWarning = siteUser.isUserWarning();
        String Gender =siteUser.getGender();
        String living =siteUser.getLiving();
        List<SiteUser> userList = userService.getFourUsers(Gender,living); // 사용자 정보를 가져오는 예시 메서드
        model.addAttribute("userList", userList);
        model.addAttribute("userWarning", userWarning);

        if(userWarning== true){
            userService.resetUserWarning(siteUser);
        }

        List<Notification> notificationsList = notificationService.getByUserTo(loginUser);
        return "th/main";
    }

    @RequestMapping("/jsp")
    public String jsp(){


        return "jsp/close";
    }

    @GetMapping("/jsp2")
    public String jsp2(){


        return "jsp/WelStdPayRequest";
    }


    @GetMapping("jsp/WelStdPayReturn")
    public String jsp3(){

        System.out.println("asdasd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("asdasd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("asdasd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("asdasd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return "jsp/WelStdPayReturn";
    }

    @GetMapping("/jsp/WelStdPayResult")
    public String jsp4(){


        return "jsp/WelStdPayResult";
    }
}