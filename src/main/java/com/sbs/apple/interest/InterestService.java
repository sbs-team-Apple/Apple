package com.sbs.apple.interest;

import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InterestService {
    private final InterestRepository interestRepository;
    public void add_interest(SiteUser siteUser,String interest_user) {
        Interest i = new Interest();
        i.setSiteUser(siteUser);
        i.setInterestUser(interest_user);
        i.setInterestedUser(siteUser.getUsername());
        this.interestRepository.save(i);
    }
    public List<Interest> getWishUsers(String Interest_user) {
        List<Interest> wishUsers;
        wishUsers = this.interestRepository.findAllByInterestUser(Interest_user);

        return  wishUsers;
    }
}
