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
    public void addInterest(SiteUser siteUser,SiteUser receivedSiteUser) {
        Interest interest = new Interest();
        interest.setSiteUser(siteUser);
        interest.setReceivedSiteUser(receivedSiteUser);
        interestRepository.save(interest);
    }

    public void removeInterest(SiteUser siteUser, SiteUser receivedSiteUser) {
        List<Interest> interests = interestRepository.findAllBySiteUserAndReceivedSiteUser(siteUser, receivedSiteUser);
        if (!interests.isEmpty()) {
            interestRepository.delete(interests.get(0));
        }
    }

    public boolean isInterested(SiteUser siteUser, SiteUser receivedSiteUser) {
        //이거 함수 이름 바꿔야 됨
        List<Interest> interests = interestRepository.findAllBySiteUserAndReceivedSiteUser(siteUser,receivedSiteUser);
        //비어 있으면 관심등록이 안된거고 차있으면 관심등록이 되어 있는 거
        return !interests.isEmpty();
    }

    public List<Interest> getSiteUser(SiteUser siteUser) {
        List<Interest> wishUsers;
        wishUsers = this.interestRepository.findAllBySiteUser(siteUser);

        return  wishUsers;
    }
    public List<Interest> getReceivedSiteUser(SiteUser receivedSiteUser) {
        List<Interest> wishUsers;
        wishUsers = this.interestRepository.findAllByReceivedSiteUser(receivedSiteUser);

        return  wishUsers;
    }
}