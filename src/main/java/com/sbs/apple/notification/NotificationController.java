package com.sbs.apple.notification;


import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class NotificationController {
    private final NotificationService notificationService;
    private  final UserService userService;


    //아작스에서 쓰이는 함수
    @GetMapping("/getNewNotifications")
    @ResponseBody
    public List<NotificationDTO> getNewNotifications(Principal principal, Model model) {
        // 여기에서 실제로 새로운 알림 정보를 가져오는 로직을 구현
        // 반환된 데이터는 클라이언트로 전송됨
        SiteUser loginUser = userService.getUserbyName(principal.getName());
        List<Notification> newNotifications = notificationService.getByUserTo(loginUser);

        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : newNotifications) {
            NotificationDTO dto = new NotificationDTO(notification);
            notificationDTOs.add(dto);
        }


        return notificationDTOs;
    }
}
