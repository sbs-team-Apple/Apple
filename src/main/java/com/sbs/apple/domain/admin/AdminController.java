package com.sbs.apple.domain.admin;

import com.sbs.apple.domain.exchange.Exchange;
import com.sbs.apple.domain.exchange.ExchangeService;
import com.sbs.apple.domain.report.Report;
import com.sbs.apple.domain.report.ReportService;
import com.sbs.apple.domain.user.SiteUser;
import com.sbs.apple.domain.user.UserService;
import com.sbs.apple.util.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ReportService reportService;
    private final AdminService adminService;
    private final ExchangeService exchangeService;
    private final Rq rq;

    @GetMapping("/admin/grantAuthority")
    public String grantAuthorityForm(){

        return "/admin/grantAuthorityForm";
    }

    @PostMapping("/admin/grantAuthority")
    public String grantAdminAuthority(@RequestParam String adminCode){

        // adminCode 유효성 확인
        if(!adminCode.equals("dipuc1023")){
            return "redirect:/admin/grantAuthority?error=true&type=adminCode";
        }

        // 해당 계정에 ADMIN 권한 추가
        adminService.grantAdminAuthority(rq.getUser());


        return "redirect:/";
    }

    @GetMapping("/admin/revokeAuthority")
    public String revokeAdminAuthority(){

        // 해당 계정의 관리자 권한 철회, 박탈
        adminService.revokeAdminAuthority(rq.getUser());

        return "redirect:/";
    }

    @GetMapping("/report_list")
    public String report_list(Model model) {
        List<Report> reportList = this.reportService.getList();
        model.addAttribute("reportList", reportList);
        return "report_list";
    }

    @GetMapping("/punish/{id}")
    public String punish(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("id", id);
        return "punish";
    }

    //영구 정지
    @GetMapping("/Permanent_stop/{id}")
    public String Permanent_stop(@PathVariable Integer id) {
        SiteUser siteUser = this.userService.getUser(id);
        userService.changeUserStop(siteUser);
        return "redirect:/";
    }

    //3일 정지
    @GetMapping("/Day3_stop/{id}")
    public String Day_stop(@PathVariable Integer id) {
        SiteUser siteUser = this.userService.getUser(id);
        userService.changeUserStop(siteUser);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            userService.resetUserStop(siteUser);
        }, 3, TimeUnit.DAYS);
        return "redirect:/";
    }

    //경고 알림
    @GetMapping("/warning/{id}")
    public String warning(@PathVariable Integer id) {
        SiteUser siteUser = this.userService.getUser(id);
        userService.changeUserWarning(siteUser);
        return "redirect:/";
    }

    @GetMapping("/exchange_list")
    public String exchangeList(Model model) {
        List<Exchange> exchangeList = exchangeService.getList();
        model.addAttribute("exchangeList", exchangeList);
        return "pay/exchange_list"; // 수정: 환전 목록 템플릿 이름
    }
}