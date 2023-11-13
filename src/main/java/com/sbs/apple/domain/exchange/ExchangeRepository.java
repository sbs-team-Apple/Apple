package com.sbs.apple.domain.exchange;

import com.sbs.apple.domain.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

    Optional<Exchange> findBySiteUser(SiteUser siteUser);
}