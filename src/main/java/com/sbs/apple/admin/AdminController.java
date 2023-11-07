package com.sbs.apple.admin;

import com.sbs.apple.exchange.Exchange;
import com.sbs.apple.exchange.ExchangeService;
import com.sbs.apple.report.Report;
import com.sbs.apple.report.ReportService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import com.sbs.apple.util.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    @GetMapping("/")
//    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
//        Page<User> paging = this.userService.getList(page);
//        model.addAttribute("paging", paging);
//        return "/admin/users";
//    }
//
//    @GetMapping("/grantAuthority")
//    public String grantAuthorityForm() {
//
//        return "/admin/grantAuthorityForm";
//    }
//
//    @PostMapping("/grantAuthority")
//    public String grantAdminAuthority(@RequestParam String adminCode) {
//
//        // adminCode 유효성 확인
//        if (!adminCode.equals("admin")) {
//            return "redirect:/grantAuthority?error=true&type=adminCode";
//        }
//
//        // 해당 계정에 ADMIN 권한 추가
//        adminService.grantAdminAuthority(rq.getUser());
//
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/revokeAuthority")
//    public String revokeAdminAuthority() {
//
//        // 해당 계정의 관리자 권한 철회, 박탈
//        adminService.revokeAdminAuthority(rq.getUser());
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/suspend/{userId}")
//    public String suspendUser(@PathVariable Long userId) {
//        adminService.suspendUser(userId);
//        return "redirect:/"; // 사용자 목록 페이지로 리다이렉트
//    }
//
//    @GetMapping("/activate/{userId}")
//    public String activateUser(@PathVariable Long userId) {
//        adminService.activateUser(userId);
//        return "redirect:/"; // 사용자 목록 페이지로 리다이렉트
//    }

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
    public String exchange_list(Model model) {
        List<Exchange> exchangeList = exchangeService.getList();
        model.addAttribute("exchangeList", exchangeList);
        return "pay/exchange_list"; // exchange_list.html을 렌더링하는 템플릿 이름
    }
}

