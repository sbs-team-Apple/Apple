package com.sbs.apple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/")
    public String root() {
        return "main";
    }

    @GetMapping("login")
    public String login() {
        return "login_form";
    }
}
