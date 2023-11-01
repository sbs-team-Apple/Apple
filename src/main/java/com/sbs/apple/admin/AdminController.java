package com.sbs.apple.admin;

import com.sbs.apple.report.Report;
import com.sbs.apple.report.ReportService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
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

    @GetMapping("/report_list")
    public String report_list(Model model){
        List<Report> reportList = this.reportService.getList();
        model.addAttribute("reportList",reportList);
        return "report_list";
    }

    @GetMapping("/punish/{id}")
    public String punish(Model model, @PathVariable("id") Integer id){
        model.addAttribute("id",id);
        return "punish";
    }
    //영구 정지
    @GetMapping("/Permanent_stop/{id}")
    public String Permanent_stop(@PathVariable Integer id){
        SiteUser siteUser = this.userService.getUser(id);
        userService.changeUserStop(siteUser);
        return "redirect:/";
    }

    //3일 정지
    @GetMapping("/Day3_stop/{id}")
    public String Day_stop(@PathVariable Integer id){
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
    public String warning(@PathVariable Integer id){
        SiteUser siteUser = this.userService.getUser(id);
        userService.changeUserWarning(siteUser);
        return "redirect:/";
    }
}