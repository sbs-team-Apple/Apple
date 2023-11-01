
package com.sbs.apple.util;


import com.sbs.apple.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataCreator {
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
            userService.createUser(username, gender);
        }
    }
}