package com.sbs.apple.exchange;

import com.sbs.apple.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

    List<Exchange> findBySiteUser(SiteUser siteUser);
}