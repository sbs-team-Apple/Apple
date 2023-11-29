package com.sbs.apple.notification;


import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class NotificationController {
    private final NotificationService notificationService;
    private  final UserService userService;

    public record YourRequestBodyClass(Integer notificationId){

    }


    //아작스에서 쓰이는 함수
    @GetMapping("/getNewNotifications")
    @ResponseBody
    public NotificationDTO getNewNotifications(Principal principal, Model model) {
        // 여기에서 실제로 새로운 알림 정보를 가져오는 로직을 구현
        // 반환된 데이터는 클라이언트로 전송됨
        SiteUser loginUser = userService.getUserbyName(principal.getName());
        Notification newNotification = notificationService.getByUserTo(loginUser);

        List<NotificationDTO> notificationDTOs = new ArrayList<>();
            NotificationDTO dto = new NotificationDTO(newNotification);
            notificationDTOs.add(dto);



        return no;
    }


    @PostMapping("/delete/notification")
    @ResponseBody
    public String deleteNotification(@RequestBody YourRequestBodyClass requestBody){
        Integer notificationId = requestBody.notificationId();

        System.out.println(notificationId+"  ㅇㄴㅁ 알림 아이디!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Notification notification=notificationService.findById(notificationId);
                notificationService.delete(notification);

        System.out.println("알림 삭제함수실행됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return "성공";
    }




}
