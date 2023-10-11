package com.sbs.apple.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myPage")
    public String userMyPage(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserbyName(username);
        model.addAttribute("user", user);
        return "myPage";
    }
}
