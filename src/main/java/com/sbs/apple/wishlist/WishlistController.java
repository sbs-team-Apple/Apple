package com.sbs.apple.whislist;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import com.sbs.apple.wishlist.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
@Controller
@RequestMapping("/wishlist")
public class WishlistController {
    @Autowired
    private UserService userService;
    @Autowired
    private WishlistRepository wishlistRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String addToWishlist(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser ownerUser = userService.getUserbyName(username);

        if (ownerUser != null) {
            List<SiteUser> addedUsers = wishlistRepository.findByOwner(ownerUser.getId().toString());
            model.addAttribute("addedUsers", addedUsers);
        }

        return "add";
    }


    @PreAuthorize("isAuthenticated")
    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestBody Map<String, String> requestData, Principal principal) {
        String addedUserId = requestData.get("userId");
        String username = principal.getName();

        System.out.println("addedUserId : " + addedUserId);
        System.out.println("username : " + username);

        SiteUser ownerUser = userService.getUserbyName(username);
//        SiteUser addedUser = userService.getUserbyId(addedUserId);

        if (ownerUser != null) {
            SiteUser siteUser = new SiteUser();
            siteUser.setOwner(ownerUser.getId().toString());
            // ownerUser를 문자열로 변환하여 할당
//            siteUser.setAddedUser(addedUser.toString()); // addedUser를 문자열로 변환하여 할당

            // 데이터베이스에 저장
            wishlistRepository.save(siteUser);
            System.out.println("siteUser__ : " + siteUser);

            return ResponseEntity.ok("사용자가 찜 목록에 추가되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("사용자 또는 소유자를 찾을 수 없습니다.");
        }
    }

}