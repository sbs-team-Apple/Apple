package com.sbs.apple.interest;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
@Controller
public class InterestController {
    @Autowired
    private InterestService interestService;
    @Autowired
    private UserService userService;

    //관심있는 사람 추가하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/add_interest/{id}")
    public String toggleInterest(Principal principal, @PathVariable Integer id, Model model) {
        String username = principal.getName();
        SiteUser siteUser = userService.getUserbyName(username);
        SiteUser receivedSiteUser =userService.getUser(id);
        model.addAttribute("userId", id);
        boolean isInterested = interestService.isInterested(siteUser,receivedSiteUser);
        if (isInterested) {
            interestService.removeInterest(siteUser,receivedSiteUser);
        } else {
            interestService.addInterest(siteUser,receivedSiteUser);
        }
        return "redirect:/user/detail/{id}";
    }


    //내가 관심있는 사람 조회하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/wish")
    public String showWish(Principal principal, Model model) {
        String username = principal.getName();
        SiteUser siteUser = userService.getUserbyName(username);

        List<Interest> siteUserList = interestService.getSiteUser(siteUser);
        model.addAttribute("siteUserList", siteUserList);

        List<Interest> receivedSiteUserList = interestService.getReceivedSiteUser(siteUser);
        model.addAttribute("receivedSiteUserList", receivedSiteUserList);
        return "wish";
    }

}

