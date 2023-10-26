package com.sbs.apple.wishlist;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/wishlist")
public class WishlistController {
    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("add")
    public String showWish(Model model, Principal principal) {

        String username = principal.getName();
        SiteUser siteUser =userService.getUserbyName(username);
        List<SiteUser> userList = userService.getWishUsers();
        model.addAttribute("userList", userList);
        return "add";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("add")
    @ResponseBody
    public String addToWishlist(@RequestParam("userId") int userId, Principal principal) {
        // 현재 로그인한 사용자 정보 가져오기
        String username = principal.getName();
        SiteUser currentUser = userService.getUserbyName(username);

        // 관심목록에 추가할 유저 정보 가져오기
        SiteUser userToAdd = userService.getUser(userId);

        // userToAdd를 currentUser의 관심목록에 추가
         userService.addUserToWishlist(currentUser, userToAdd);  // 이렇게 추가하는 메소드를 만들어야 함

        return "success";  // 또는 적절한 응답을 반환
    }
}

