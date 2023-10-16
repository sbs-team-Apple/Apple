package com.sbs.apple.cybermoney;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/cybermoney")
public class CyberMoneyController {
    private final CyberMoneyService cyberMoneyService;
    private final UserRepository siteUserRepository;

    public CyberMoneyController(CyberMoneyService cyberMoneyService, UserRepository siteUserRepository) {
        this.cyberMoneyService = cyberMoneyService;
        this.siteUserRepository = siteUserRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCyberMoney(
            @RequestParam("recipientUsername") String recipientUsername,
            @RequestParam("amount") int amount
    ) {
        // 현재 로그인한 사용자를 가져옵니다.
        SiteUser senderUser = (SiteUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 보내는 사람과 받는 사람이 다른 사용자인지 확인합니다.
        if (senderUser.getUsername().equals(recipientUsername)) {
            return ResponseEntity.badRequest().body("자기 자신에게 사이버 머니를 보낼 수 없습니다.");
        }

        // 받는 사용자를 찾습니다.
        Optional<SiteUser> recipientUserOptional = siteUserRepository.findByusername(recipientUsername);
        if (!recipientUserOptional.isPresent()) {
            return ResponseEntity.badRequest().body("받는 사용자를 찾을 수 없습니다.");
        }
        SiteUser recipientUser = recipientUserOptional.get();

        // 사이버 머니를 보냅니다.
        try {
            cyberMoneyService.sendCyberMoney(senderUser, recipientUser, amount);
            return ResponseEntity.ok("사이버 머니 전송이 성공했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
