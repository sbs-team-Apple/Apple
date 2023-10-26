package com.sbs.apple.interest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {

    List<Interest> findAllByInterestUser(String interestUser);

}
