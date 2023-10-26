package com.sbs.apple.interest;

import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InterestService {
    private final InterestRepository interestRepository;
    public void add_interest(SiteUser siteUser,String interest_user) {
        Interest i = new Interest();
        i.setSiteUser(siteUser);
        i.setInterest_user(interest_user);
        i.setInterested_user(siteUser.getUsername());
        this.interestRepository.save(i);
    }
}
