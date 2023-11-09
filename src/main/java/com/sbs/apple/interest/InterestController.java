package com.sbs.apple.interest;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/add_interest/{id}")
    public String toggleInterest(Principal principal, @PathVariable Integer id, Model model) {
        String interest_user = principal.getName();
        model.addAttribute("userId", id);
        boolean isInterested = interestService.isInterested(id, interest_user);
        if (isInterested) {
            interestService.removeInterest(id, interest_user);
        } else {
            interestService.addInterest(id, interest_user);
        }
        return "redirect:/user/detail/{id}";
    }


    //내가 관심있는 사람 조회하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/wish")
    public String showWish(Principal principal, Model model) {
        String username = principal.getName();
        List<Interest> interestList = interestService.getWishUsers(username);
        model.addAttribute("interestList", interestList);
        return "wish";
    }

    //나에게 관심 있는 사람 조회하기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/wished")
    public String showWished(Principal principal, Model model) {
        String username = principal.getName();
        List<Interest> interestList = interestService.getWishedUsers(username);
        model.addAttribute("interestList", interestList);
        return "wished";
    }
}



