package com.sbs.apple.domain.exchange;

import com.sbs.apple.domain.user.SiteUser;
import com.sbs.apple.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@RequestMapping("/exchange")
@RequiredArgsConstructor
@Controller
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Exchange> exchangeList = this.exchangeService.getList();
        model.addAttribute("exchangeList", exchangeList);
        return "pay/exchange_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Exchange exchange = this.exchangeService.getExchange(id);
        model.addAttribute("exchange", exchange);
        return "pay/exchange_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String exchangeCreate(ExchangeForm exchangeForm, Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserbyName(username);

        int receivedCyberMoney = user.getReceivedCyberMoney(); // 다른 사용자로부터 받은 사이버머니

        model.addAttribute("receivedCyberMoney", receivedCyberMoney); // 다른 사용자로부터 받은 사이버머니
        return "pay/exchange_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String exchangeCreate(@Valid ExchangeForm exchangeForm, RedirectAttributes redirectAttributes,
                                 BindingResult bindingResult, Principal principal,Model model) {
        if (bindingResult.hasErrors()) {
            return "pay/exchange_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.exchangeService.create(exchangeForm.getSubject(), exchangeForm.getContent(), siteUser);
        redirectAttributes.addAttribute("id", siteUser.getId());


        return "redirect:/exchange/exchange_apply";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exchange_apply")
    public String exchange_apply(ExchangeForm exchangeForm, Model model, Principal principal) {
        String username = principal.getName();
        SiteUser siteUser = userService.getUserbyName(username);

        model.addAttribute("siteUser", siteUser);

        return "pay/exchange_apply";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/exchange_apply")
    public String exchange_apply(@Valid ApplyForm applyForm, RedirectAttributes redirectAttributes,
                                 BindingResult bindingResult, Principal principal,Model model) {
        if (bindingResult.hasErrors()) {
            return "pay/exchange_apply";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Exchange exchange =this.exchangeService.getExchangeBySiteUser(siteUser);
        this.exchangeService.apply(exchange, applyForm.getRealname(), applyForm.getEmail(), applyForm.getAddress(),
                applyForm.getF_No(),applyForm.getPhonNo_2(),
                applyForm.getPhonNo_3(),applyForm.getHomeAdress(),
                applyForm.getNationality(),applyForm.getBank(),
                applyForm.getAccountHolder(),applyForm.getAccountNumber());
        return "redirect:/";
    }
}