package com.sbs.apple.exchange;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ExchangeController {

    private UserService userService;
    private ExchangeService exchangeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exchange")
    public String exchange(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserbyName(username);
        model.addAttribute("user", user);

        int receivedCyberMoney = user.getReceivedCyberMoney(); // 다른 사용자로부터 받은 사이버머니

        model.addAttribute("receivedCyberMoney", receivedCyberMoney); // 다른 사용자로부터 받은 사이버머니

        return "pay/exchange";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exchange_apply")
    public String exchange(Principal principal, Model model) {
        String username = principal.getName();
        SiteUser siteUser = userService.getUserbyName(username);

        model.addAttribute("siteUser", siteUser);

        return "pay/exchange_apply";
    }

    @PostMapping("/exchange_apply")
    public String exchangeCreate(Principal principal) {
        System.out.println("환전신청 실행됨");
        String username = principal.getName();
        SiteUser siteUser = userService.getUserbyName(username);

        exchangeService.create(siteUser); // Exchange 객체를 저장

        return "redirect:/";
    }
}


