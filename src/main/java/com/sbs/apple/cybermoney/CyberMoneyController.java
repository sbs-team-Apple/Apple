package com.sbs.apple.cybermoney;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/cybermoney")
public class CyberMoneyController {
    @Autowired
    private CyberMoneyTransferService transferService;

    @PostMapping("/transfer")
    public String transferCyberMoney(HttpServletRequest request,
                                     @RequestParam String recipientUsername,
                                     @RequestParam int amount,
                                     RedirectAttributes attributes) {
        try {
            Principal principal = request.getUserPrincipal();
            String senderUsername = principal.getName();

            // 사용자의 사이버 머니 전송을 처리하는 서비스 호출
            transferService.transferCyberMoney(senderUsername, recipientUsername, amount);

            attributes.addFlashAttribute("successMessage", amount + " 사이버 머니를 전송했습니다.");
        } catch (InsufficientFundsException e) {
            attributes.addFlashAttribute("errorMessage", "사이버 머니가 부족합니다.");
        } catch (UserNotFoundException e) {
            attributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
        }

        return "redirect:/";
    }
}
