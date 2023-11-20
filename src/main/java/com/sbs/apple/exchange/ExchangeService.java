package com.sbs.apple.exchange;

import com.sbs.apple.DataNotFoundException;
import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;

    public List<Exchange> getList() {
        return this.exchangeRepository.findAll();
    }

    public Exchange getExchange(Integer id) {
        Optional<Exchange> exchange = this.exchangeRepository.findById(id);
        if (exchange.isPresent()) {
            return exchange.get();
        } else {
            throw new DataNotFoundException("exchange not found");
        }
    }
    public Exchange getExchangeBySiteUser(SiteUser siteUser) {
        Optional<Exchange> exchange = this.exchangeRepository.findBySiteUser(siteUser);
        if (exchange.isPresent()) {
            return exchange.get();
        } else {
            throw new DataNotFoundException("exchange not found");
        }
    }
    public void create(String subject, String content, SiteUser user) {
        Exchange exchange = new Exchange();
        exchange.setSubject(subject);
        exchange.setContent(content);
        exchange.setCreateDate(LocalDateTime.now());
        exchange.setSiteUser(user);
        this.exchangeRepository.save(exchange);
    }
    public void apply(Exchange exchange, String realname, String email,
                      String address, String f_No, String phonNo_2,
                      String phonNo_3, String homeAdress, String nationality,
                      String bank, String accountHolder, String accountNumber
                      ) {
        exchange.setRealname(realname);
        exchange.setEmail(email);
        exchange.setAddress(address);
        exchange.setF_No(f_No);
        exchange.setPhonNo_2(phonNo_2);
        exchange.setPhonNo_3(phonNo_3);
        exchange.setHomeAdress(homeAdress);
        exchange.setNationality(nationality);
        exchange.setBank(bank);
        exchange.setAccountHolder(accountHolder);
        exchange.setAccountNumber(accountNumber);
        exchange.setCreateDate(LocalDateTime.now());
        this.exchangeRepository.save(exchange);
    }
}