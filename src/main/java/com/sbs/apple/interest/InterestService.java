package com.sbs.apple.interest;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserRepository;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InterestService {
    private final InterestRepository interestRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    public void addInterest(Integer siteUserId, String interestUserName) {
        SiteUser interestUser =this.userService.getUserbyName(interestUserName);
        SiteUser siteUser = userService.getUser(siteUserId);
        Interest interest = new Interest();
        interest.setSiteUser(siteUser);
        interest.setFilepath(interestUser.getFilepath());
        interest.setInterestUserId(interestUser.getId());
        interest.setInterestUser(interestUserName);
        interest.setInterestedUser(siteUser.getUsername());
        interestRepository.save(interest);
    }

    public void removeInterest(Integer siteUserId, String interestUser) {
        // Find and delete the interest entry
        List<Interest> interests = interestRepository.findAllBySiteUser_IdAndInterestUser(siteUserId, interestUser);
        if (!interests.isEmpty()) {
            interestRepository.delete(interests.get(0));
        }
    }

    public boolean isInterested(Integer siteUserId, String interestUser) {
        List<Interest> interests = interestRepository.findAllBySiteUser_IdAndInterestUser(siteUserId, interestUser);
        //비어 있으면 관심등록이 안된거고 차있으면 관심등록이 되어 있는 거
        return !interests.isEmpty();
    }

    public List<Interest> getWishUsers(String Interest_user) {
        List<Interest> wishUsers;
        wishUsers = this.interestRepository.findAllByInterestUser(Interest_user);

        return  wishUsers;
    }
    public List<Interest> getWishedUsers(String Interested_user) {
        List<Interest> wishUsers;
        wishUsers = this.interestRepository.findAllByInterestedUser(Interested_user);

        return  wishUsers;
    }
}