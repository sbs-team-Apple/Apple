package com.sbs.apple.exchange;

import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;

    public void create(SiteUser siteUser) {
        Exchange exchange = new Exchange();
        exchange.setSiteUser(siteUser);
        this.exchangeRepository.save(exchange);
    }

    public List<Exchange> getList() {
        return this.exchangeRepository.findAll();
    }
}
