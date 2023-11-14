package com.sbs.apple.interest;

import com.sbs.apple.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    List<Interest> findAllBySiteUser(SiteUser siteUser);
    List<Interest> findAllByReceivedSiteUser(SiteUser receivedSiteUser);

    List<Interest> findAllBySiteUserAndReceivedSiteUser(SiteUser siteUser, SiteUser receivedSiteUser);

}