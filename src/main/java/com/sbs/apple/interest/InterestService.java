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
    public void addInterest(Integer siteUserId, String interestUser) {
        SiteUser siteUser = userService.getUser(siteUserId);

        Interest interest = new Interest();
        interest.setSiteUser(siteUser);
        interest.setInterestUser(interestUser);
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
        // Check if the user is interested in the specified user
        List<Interest> interests = interestRepository.findAllBySiteUser_IdAndInterestUser(siteUserId, interestUser);
        return !interests.isEmpty();
    }

    public List<Interest> getWishUsers(String Interest_user) {
        List<Interest> wishUsers;
        wishUsers = this.interestRepository.findAllByInterestUser(Interest_user);

        return  wishUsers;
    }
}

