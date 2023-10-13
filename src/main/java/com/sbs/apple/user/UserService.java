package com.sbs.apple.user;

import com.sbs.apple.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser getUser(Integer id){
        Optional<SiteUser> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }
    public SiteUser getUserbyName(String name){
        Optional<SiteUser> user = this.userRepository.findByusername(name);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public SiteUser create(String username, String password, String nickname, String gender) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        user.setGender(gender);
        this.userRepository.save(user);
        return user;
    }

    public SiteUser add_profile(SiteUser user, int age, String living, String hobby, int tall, String bodyType,
                                boolean smoking, String drinking, String style, String religion,
                                String mbti, String school, String job) {
        user.setAge(age);
        user.setLiving(living);
        user.setHobby(hobby);
        user.setTall(tall);
        user.setBody_type(bodyType);
        user.setSmoking(smoking);
        user.setDrinking(drinking);
        user.setStyle(style);
        user.setReligion(religion);
        user.setMbti(mbti);
        user.setSchool(school);
        user.setJob(job);
        this.userRepository.save(user);
        return user;
    }


    public SiteUser add_desired(SiteUser user, String desiredAge, String desiredLiving, String desiredHobby,
                                String desiredTall, String desiredBodyType, String desiredSmoking,
                                String desiredDrinking, String desiredStyle, String desiredReligion,
                                String desiredMbti) {
        user.setDesired_age(desiredAge);
        user.setDesired_living(desiredLiving);
        user.setDesired_hobby(desiredHobby);
        user.setDesired_tall(desiredTall);
        user.setDesired_body_type(desiredBodyType);
        user.setDesired_smoking(desiredSmoking);
        user.setDesired_drinking(desiredDrinking);
        user.setDesired_style(desiredStyle);
        user.setDesired_religion(desiredReligion);
        user.setDesired_mbti(desiredMbti);
        this.userRepository.save(user);
        return user;
    }
    public boolean isCorrectPassword(String username, String password) {
        SiteUser user = getUserbyName(username);
        String actualPassword = user.getPassword();
        return passwordEncoder.matches(password, actualPassword);
    }
    public void updatePassword(String username, String newPassword) {
        SiteUser user = getUserbyName(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public void delete(SiteUser siteUser) {
        // 해당 사용자와 연결된 answer 레코드 삭제
//        List<Answer> userAnswers = answerRepository.findByAuthor(siteUser);
//        answerRepository.deleteAll(userAnswers);

        // 해당 사용자와 연결된 question 레코드 삭제
//        List<Question> userQuestions = questionRepository.findByAuthor(siteUser);
//        questionRepository.deleteAll(userQuestions);

        // 사용자 삭제
        this.userRepository.delete(siteUser);
    }
    //소셜 로그인
    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String username, String nickname) {
        // 소셜 로그인를 통한 가입시 비번은 없다.
        return create(username, "",nickname,""); // 최초 로그인 시 딱 한번 실행
    }

    public List<SiteUser> getFourUsers() {
        List<SiteUser> randomUsers = this.userRepository.findRandomUsers(4);
        return randomUsers;
    }
}
