package com.sbs.apple.payment;

import com.sbs.apple.user.UserRepository;
import com.sbs.apple.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

}


