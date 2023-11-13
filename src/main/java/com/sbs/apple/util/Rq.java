package com.sbs.apple.util;

import com.sbs.apple.domain.notification.Notification;
import com.sbs.apple.domain.notification.NotificationService;
import com.sbs.apple.domain.user.SiteUser;
import com.sbs.apple.domain.user.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@Getter
@RequestScope
public class Rq {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final HttpSession session;
    private SiteUser siteUser;
    private boolean login;
    private final UserService userService;
    private final NotificationService notificationService;

    public Rq(UserService userService, NotificationService notificationService) {
        ServletRequestAttributes sessionAttributes = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()));
        HttpServletRequest request = sessionAttributes.getRequest();
        HttpServletResponse response = sessionAttributes.getResponse();
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.userService = userService;
        this.notificationService = notificationService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            siteUser = (SiteUser) authentication.getPrincipal();
        } else if (authentication instanceof AnonymousAuthenticationToken) {
            siteUser = new SiteUser();
            siteUser.setId((int) -1);
            siteUser.setUsername("");
        }
    }

    public void forward(String forwardUrl) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.getMessage();
        }
    }

    public String unexpectedRequestForWardUri(String msg) {
        request.setAttribute("msg", msg);
        return "forward:/404";
    }

    public Cookie getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public boolean isAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        for(GrantedAuthority grantedAuthority: grantedAuthorities){

            // admin 권한 확인
            if(grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                return true;
            }
        }

        return false;
    }

    public boolean isLogin() {
        return siteUser != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }

    private String getLoginedMemberUsername() {
        if (isLogout()) return null;
        return siteUser.getUsername();
    }

    public SiteUser getUser() {
        if (isLogout()) {
            return null;
        }
        if (siteUser == null) {
            siteUser = userService.getUserbyName(getLoginedMemberUsername());
        }
        return siteUser;
    }

    public List<Notification> getNotification() {
        if (isLogout()) {
            return null;
        }
        List<Notification> notificationList = new ArrayList<>();
        siteUser = userService.getUserbyName(getLoginedMemberUsername());
        notificationList = notificationService.getByUserTo(siteUser);
        return notificationList;
    }
}
