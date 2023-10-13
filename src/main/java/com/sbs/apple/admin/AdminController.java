package com.sbs.apple.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminAccountService adminAccountService;

    @Autowired
    public AdminController(AdminAccountService adminAccountService) {
        this.adminAccountService = adminAccountService;
    }

    @GetMapping("/createAdminAccount")
    public String createAdminAccount() {
        adminAccountService.createAdminAccount();
        return "Admin account created!";
    }

    @GetMapping("/report_list")
    public String report_list(){
        return "report_list";
    }

}
