package com.sbs.apple.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    List<Interest> findAllByInterestUser(String interestUser);
    List<Interest> findAllByInterestedUser(String interestedUser);

    List<Interest> findAllBySiteUser_IdAndInterestUser(Integer siteUserId, String interestUser);
}