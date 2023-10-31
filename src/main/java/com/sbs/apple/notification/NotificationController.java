package com.sbs.apple.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class NotificationController {
    private final NotificationService notificationService;
}
