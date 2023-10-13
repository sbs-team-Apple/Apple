package com.sbs.apple.user;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

//회원가입
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @GetMapping("/signup")
    public String signup1(UserCreateForm userCreateForm) {
        return "user/signup_form";
    }

    @PostMapping("/signup")
    public String signup2(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user/signup_form";
        }
        //adsdfur
        SiteUser user= userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(),
                userCreateForm.getNickname(), userCreateForm.getGender());
        redirectAttributes.addAttribute("id", user.getId());
        return "redirect:/user/add/" + user.getId();
    }


   @GetMapping("/add/{id}")
   public String add1(UserAddForm userAddForm,@PathVariable("id") Integer id, Model model){
       model.addAttribute("userId", id);
        return "user/add_form";
   }
    @PostMapping("/add/{id}")
    public String add2(@PathVariable("id") Integer id,UserAddForm userAddForm,RedirectAttributes redirectAttributes) {
        SiteUser user = this.userService.getUser(id);
        userService.add_profile(user,userAddForm.getAge(),userAddForm.getLiving(),userAddForm.getHobby(),
                userAddForm.getTall(),userAddForm.getBody_type(),userAddForm.isSmoking(),
                userAddForm.getDrinking(),userAddForm.getStyle(),userAddForm.getReligion(),
                userAddForm.getMbti(),userAddForm.getSchool(),userAddForm.getJob());
        redirectAttributes.addAttribute("id", user.getId());
        return "redirect:/user/desired/" + user.getId();
    }
    //원하는 이상형 작성
    @GetMapping("/desired/{id}")
    public String desired1(UserDesiredForm userDesiredForm,@PathVariable("id") Integer id, Model model) {
        model.addAttribute("userId",id);
        return "user/desired_form";
    }

    @PostMapping("/desired/{id}")
    public String desired2(@PathVariable("id") Integer id,UserDesiredForm userDesiredForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/desired_form";
        }
        SiteUser user = this.userService.getUser(id);
        userService.add_desired(user,userDesiredForm.getDesired_age(),userDesiredForm.getDesired_living(),
                userDesiredForm.getDesired_hobby(),userDesiredForm.getDesired_tall(),
                userDesiredForm.getDesired_body_type(),userDesiredForm.getDesired_smoking(),
                userDesiredForm.getDesired_drinking(),userDesiredForm.getDesired_style(),
                userDesiredForm.getDesired_religion(),userDesiredForm.getDesired_mbti());
        return "redirect:/";
    }

    //로그인
    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }
    //마이 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String userMyPage(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserbyName(username);

        int userCyberMoney = user.getCyberMoney();
        model.addAttribute("userCyberMoney", userCyberMoney);
        model.addAttribute("user", user);
        return "myPage";
    }
    //비밀번호 변경
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/passwordChange")
    public String passwordChange(Model model) {
        return "user/passwordChange";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/passwordChange")
    public String passwordChange(@RequestParam("password") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, Principal principal) {
        String username = principal.getName();
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "user/passwordChange";
        }

        if (!userService.isCorrectPassword(username, currentPassword)) {
            model.addAttribute("error", "기존 비밀번호가 올바르지 않습니다.");
            return "user/passwordChange";
        }
        userService.updatePassword(username, newPassword);
        return "redirect:/user/myPage";
    }
    // 마이페이지 탈퇴 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String mypage_exit(Principal principal, Model model) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        model.addAttribute("siteUser", siteUser);
        return "user/delete";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete")
    public String userDelete(HttpServletRequest request, @RequestParam("password") String password,
                             HttpServletResponse response, Principal principal, RedirectAttributes attributes) {
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());

        if (BCrypt.checkpw(password, siteUser.getPassword())) {
            this.userService.delete(siteUser);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            return "redirect:/"; // 메인 페이지로 리다이렉트
        } else {
            // 비밀번호가 일치하지 않을 때 오류 메시지를 전달하고 회원 탈퇴 페이지로 리다이렉트합니다.
            attributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다. 다시 시도해주세요.");
            return "redirect:/user/delete";
        }
    }
    //프로필 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile_modify")
    public String profile_modify1(UserAddForm userAddForm,Principal principal,Model model){
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        userAddForm.setAge(siteUser.getAge()); userAddForm.setLiving(siteUser.getLiving());
        userAddForm.setHobby(siteUser.getHobby()); userAddForm.setTall(siteUser.getTall());
        userAddForm.setBody_type(siteUser.getBody_type()); userAddForm.setSmoking(siteUser.isSmoking());
        userAddForm.setDrinking(siteUser.getDrinking()); userAddForm.setStyle(siteUser.getStyle());
        userAddForm.setReligion(siteUser.getReligion()); userAddForm.setMbti(siteUser.getMbti());
        userAddForm.setSchool(siteUser.getSchool()); userAddForm.setJob(siteUser.getJob());
        model.addAttribute("siteUser", siteUser);
        return "user/profile_modify";
    }
    @PostMapping("/profile_modify")
    public String profile_modify(UserAddForm userAddForm,Principal principal) {
        SiteUser user = this.userService.getUserbyName(principal.getName());
        userService.add_profile(user,userAddForm.getAge(),userAddForm.getLiving(),userAddForm.getHobby(),
                userAddForm.getTall(),userAddForm.getBody_type(),userAddForm.isSmoking(),
                userAddForm.getDrinking(),userAddForm.getStyle(),userAddForm.getReligion(),
                userAddForm.getMbti(),userAddForm.getSchool(),userAddForm.getJob());
        return "redirect:/user/myPage";
    }
    //이상형 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/desired_modify")
    public String desired_modify1(UserDesiredForm userDesiredForm,Principal principal,Model model){
        SiteUser siteUser = this.userService.getUserbyName(principal.getName());
        userDesiredForm.setDesired_age(siteUser.getDesired_age()); userDesiredForm.setDesired_living(siteUser.getDesired_living());
        userDesiredForm.setDesired_hobby(siteUser.getDesired_hobby()); userDesiredForm.setDesired_tall(siteUser.getDesired_tall());
        userDesiredForm.setDesired_body_type(siteUser.getDesired_body_type()); userDesiredForm.setDesired_smoking(siteUser.getDesired_smoking());
        userDesiredForm.setDesired_drinking(siteUser.getDesired_drinking()); userDesiredForm.setDesired_style(siteUser.getDesired_style());
        userDesiredForm.setDesired_religion(siteUser.getDesired_religion()); userDesiredForm.setDesired_mbti(siteUser.getDesired_mbti());
        model.addAttribute("siteUser", siteUser);
        return "user/desired_modify";
    }
    @PostMapping("/desired_modify")
    public String desired_modify(UserDesiredForm userDesiredForm,Principal principal) {
        SiteUser user = this.userService.getUserbyName(principal.getName());
        userService.add_desired(user,userDesiredForm.getDesired_age(),userDesiredForm.getDesired_living(),
                userDesiredForm.getDesired_hobby(),userDesiredForm.getDesired_tall(),
                userDesiredForm.getDesired_body_type(),userDesiredForm.getDesired_smoking(),
                userDesiredForm.getDesired_drinking(),userDesiredForm.getDesired_style(),
                userDesiredForm.getDesired_religion(),userDesiredForm.getDesired_mbti());
        return "redirect:/user/myPage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment")
    public String paymentPage(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserbyName(username);
        model.addAttribute("user", user);
        return "payment";
    }

}
