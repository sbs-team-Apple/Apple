
package com.sbs.apple.util;


import com.sbs.apple.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class    DataCreator {
    private final UserService userService;
    protected Integer userNum = 40;

    @Autowired
    public DataCreator(UserService userService) {
        this.userService = userService;
    }

    public void createTestData() {
        createUser();
    }

    private void createUser() {
        for (int i = 1; i <= userNum; i++) {
            String username = "user" + i;
            String gender = i <= userNum / 2 ? "남" : "여";
            List<String> hobbyList = i <= userNum / 2 ? (Arrays.asList("골프", "축구")) : (Arrays.asList("등산", "볼링"));
            List<String> styleList = i <= userNum / 2 ? (Arrays.asList("어른스러운", "허세 없는")) : (Arrays.asList("엉뚱한", "소심한"));
            List<String> desiredStyleList = i <= userNum / 2 ? (Arrays.asList("예쁘고 잘생긴", "활발한")) : (Arrays.asList("유머러스한", "성실한"));
            userService.createUser(username, gender, hobbyList, styleList, desiredStyleList);
        }
    }
}