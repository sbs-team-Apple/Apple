package com.sbs.apple.user;

import com.sbs.apple.DataNotFoundException;
import com.sbs.apple.Hobby;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getUser(Integer id){
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public User create(String username, String password, String nickname, String gender) {
        User user = new User();
        user.setUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        user.setGender(gender);
        this.userRepository.save(user);
        return user;
    }

    public User add_profile(User user, int age, String living, List<Hobby> hobby, int tall, String bodyType,
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


    public User add_desired(User user,int desiredAge, String desiredLiving, List<Hobby> desiredHobby,
                            int desiredTall, String desiredBodyType, boolean desiredSmoking,
                            String desiredDrinking, String desiredStyle, String desiredReligion,
                            String desiredMbti) {
        user.setDesired_age(desiredAge);
        user.setDesired_living(desiredLiving);
        user.setDesired_hobby(desiredHobby);
        user.setDesired_tall(desiredTall);
        user.setDesired_body_type(desiredBodyType);
        user.setDesired_smoking(desiredSmoking);
        user.setDrinking(desiredDrinking);
        user.setDesired_style(desiredStyle);
        user.setDesired_religion(desiredReligion);
        user.setDesired_mbti(desiredMbti);
        this.userRepository.save(user);
        return user;
    }
}
