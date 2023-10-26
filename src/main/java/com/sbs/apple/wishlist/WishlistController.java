//package com.sbs.apple.wishlist;
//
//import com.sbs.apple.user.SiteUser;
//import com.sbs.apple.user.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//@RequiredArgsConstructor
//@Controller
//@RequestMapping("/wishlist")
//public class WishlistController {
//    @Autowired
//    private UserService userService;
//
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("add")
//    public String showWish(Model model, Principal principal) {
//
//        String username = principal.getName();
//        SiteUser siteUser =userService.getUserbyName(username);
//        List<SiteUser> userList = userService.getWishUsers();
//        model.addAttribute("userList", userList);
//        return "add";
//    }
//}
//
