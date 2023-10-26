package com.sbs.apple.interest;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InterestService {
    private final InterestRepository interestRepository;
    private final UserService userService;
    public void add_interest(Integer siteUserId, String interest_user) {
        SiteUser siteUser = userService.getUser(siteUserId);

        Interest i = new Interest();
        i.setSiteUser(siteUser);
        i.setInterestUser(interest_user);
        i.setInterestedUser(siteUser.getUsername());
        interestRepository.save(i);
    }

    public List<Interest> getWishUsers(String Interest_user) {
        List<Interest> wishUsers;
        wishUsers = this.interestRepository.findAllByInterestUser(Interest_user);

        return  wishUsers;
    }


}
