package com.sbs.apple.cybermoney;

import com.sbs.apple.chat.SseEmitters;
import com.sbs.apple.notification.NotificationService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/cybermoney")
public class CyberMoneyController {
    private final CyberMoneyService cyberMoneyService;
    private final UserRepository userRepository;
    private final SseEmitters sseEmitters;
    private final NotificationService notificationService;
    private final CyberMoneyTransactionRepository cyberMoneyTransactionRepository;


    @PostMapping("/JustSend")
    public ResponseEntity<String> JustsendCyberMoney(
            @RequestParam("recipientUsername") String recipientUsername,
            @RequestParam("amount") int amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<SiteUser> senderUserOptional = userRepository.findByUsername(username);
        if (!senderUserOptional.isPresent()) {
            return ResponseEntity.badRequest().body("보내는 사용자를 찾을 수 없습니다.");
        }
        SiteUser senderUser = senderUserOptional.get();

        if (senderUser.getUsername().equals(recipientUsername)) {
            return ResponseEntity.badRequest().body("자기 자신에게 사이버 머니를 보낼 수 없습니다.");
        }

        Optional<SiteUser> recipientUserOptional = userRepository.findByUsername(recipientUsername);
        if (!recipientUserOptional.isPresent()) {
            return ResponseEntity.badRequest().body("받는 사용자를 찾을 수 없습니다.");
        }
        SiteUser recipientUser = recipientUserOptional.get();


        cyberMoneyService.justsendCyberMoney(senderUser, recipientUser, amount);
        return ResponseEntity.ok("사이버 머니 전송이 성공했습니다.");
    }




    @PostMapping("/send")
    public ResponseEntity<String> sendCyberMoney(
            @RequestParam("recipientUsername") String recipientUsername,
            @RequestParam("amount") int amount
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 이후에 username을 사용하여 사용자 데이터를 불러옵니다.
        Optional<SiteUser> senderUserOptional = userRepository.findByUsername(username);
        if (!senderUserOptional.isPresent()) {
            return ResponseEntity.badRequest().body("보내는 사용자를 찾을 수 없습니다.");
        }
        SiteUser senderUser = senderUserOptional.get();

        // 보내는 사람과 받는 사람이 다른 사용자인지 확인합니다.
        if (senderUser.getUsername().equals(recipientUsername)) {
            return ResponseEntity.badRequest().body("자기 자신에게 사이버 머니를 보낼 수 없습니다.");
        }

        // 받는 사용자를 찾습니다.
        Optional<SiteUser> recipientUserOptional = userRepository.findByUsername(recipientUsername);
        if (!recipientUserOptional.isPresent()) {
            return ResponseEntity.badRequest().body("받는 사용자를 찾을 수 없습니다.");
        }
        SiteUser recipientUser = recipientUserOptional.get();

        // 사이버 머니를 보냅니다.
        try {
            cyberMoneyService.sendCyberMoney(senderUser, recipientUser, amount);

            String groupKey = "userId_" + recipientUser.getId();
            System.out.println(groupKey);
            SseEmitter emitter = new SseEmitter();
            sseEmitters.add(groupKey, emitter);
            sseEmitters.noti(groupKey, "give_money");
            notificationService.create(recipientUser,senderUser,"money" );
            return ResponseEntity.ok("사이버 머니 전송이 성공했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
