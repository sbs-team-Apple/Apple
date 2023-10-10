package com.sbs.apple.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

//회원가입
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult , Model model) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
       SiteUser user= userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(),
                userCreateForm.getNickname(), userCreateForm.getGender());

        model.addAttribute("user",user);

        return "add_form2";
    }
    //로그인
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }





   //기본 프로필 작성
    @GetMapping("/add/{id}")
    public String add(UserAddForm userAddForm, @PathVariable("id") Integer id, Model  ) {
        SiteUser user = this.userService.getUser(id);
        System.out.println(user.getUsername());
        userService.add_profile(user,userAddForm.getAge(),userAddForm.getLiving(),userAddForm.getHobby(),
                userAddForm.getTall(),userAddForm.getBody_type(),userAddForm.isSmoking(),
                userAddForm.getDrinking(),userAddForm.getStyle(),userAddForm.getReligion(),
                userAddForm.getMbti(),userAddForm.getSchool(),userAddForm.getJob());


        model.addAttribute("user",user);


        return "main";
    }

    @GetMapping("/desired")
    public String desired(UserDesiredForm userDesiredForm) {
        return "desired_form";
    }

    @PostMapping("/desired")
    public String desired(@Valid UserDesiredForm userDesiredForm, BindingResult bindingResult,
                          Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "desired_form";
        }
        SiteUser user = this.userService.getUser(id);
        userService.add_desired(user,userDesiredForm.getDesired_age(),userDesiredForm.getDesired_living(),
                userDesiredForm.getDesired_hobby(),userDesiredForm.getDesired_tall(),
                userDesiredForm.getDesired_body_type(),userDesiredForm.isDesired_smoking(),
                userDesiredForm.getDesired_drinking(),userDesiredForm.getDesired_style(),
                userDesiredForm.getDesired_religion(),userDesiredForm.getDesired_mbti());
        return "redirect:/";
    }

}
