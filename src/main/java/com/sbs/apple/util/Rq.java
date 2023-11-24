package com.sbs.apple.util;


import com.sbs.apple.Ut;
import com.sbs.apple.board.Board;
import com.sbs.apple.board.BoardService;
import com.sbs.apple.imgs.Imgs;
import com.sbs.apple.imgs.ImgsService;
import com.sbs.apple.notification.Notification;
import com.sbs.apple.notification.NotificationService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
//@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@RequestScope
public class Rq {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final HttpSession session;
    private SiteUser loginUser =null;
    private boolean login;
    private final UserService userService;
    private User user;
    private final NotificationService notificationService;
    private final ImgsService imgsService;
    private final BoardService boardService;


    public Rq(UserService userService ,NotificationService notificationService,ImgsService imgsService,BoardService boardService) {
        ServletRequestAttributes sessionAttributes = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()));
        HttpServletRequest request = sessionAttributes.getRequest();
        HttpServletResponse response = sessionAttributes.getResponse();
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.userService=userService;
        this.notificationService=notificationService;
        this.imgsService=imgsService;
        this.boardService=boardService;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        if(authentication instanceof UsernamePasswordAuthenticationToken) {
//            SiteUser user = (SiteUser) authentication.getPrincipal();
//            this.user = user;
//            this.login = true;
//        } else if(authentication instanceof AnonymousAuthenticationToken) {
//            SiteUser anonymous = new SiteUser();
//            anonymous.setId((int) -1);
//            anonymous.setUsername("");
//
//            this.user = anonymous;
//            this.login = false;
//        }

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user= null;
        }

    }

    // forwarding
    public void forward(String forwardUrl) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardUrl);
        try {
            dispatcher.forward(request, response);
        } catch(ServletException | IOException e){
            e.getMessage();
        }
    }

    // unexpected_request
    public String unexpectedRequestForWardUri(String msg){

        request.setAttribute("msg", msg);
        return "forward:/404";
    }

    public Cookie getCookie(String name){

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
        return user != null;
    }

    public boolean isLogout() {
        return !isLogin();
    }


    private String getLoginedMemberUsername() {
        if (isLogout()) return null;

        return user.getUsername();
    }
    public SiteUser getUser() {
        if (isLogout()) {
            return null;
        }
        if(loginUser ==null) {
            loginUser = userService.getUserbyName(getLoginedMemberUsername());


        }


        return loginUser;
    }


    public List<Notification> getNotification(){
        if (isLogout()) {
            return null;
        }
        List<Notification> notificationList =new ArrayList<>();


            loginUser = userService.getUserbyName(getLoginedMemberUsername());

            notificationList=notificationService.getByUserTo(loginUser);


        return notificationList;
    }

    public List<Imgs> getImgs(Board board){
        imgsService.getImgsByBoard(board);
        return imgsService.getImgsByBoard(board);
    }
    public String historyBack(String msg) {
        String referer = request.getHeader("referer");
        String key = "historyBackFailMsg___" + referer;
        request.setAttribute("localStorageKeyAboutHistoryBackFailMsg", key);
        request.setAttribute("historyBackFailMsg", Ut.url.withTtl(msg));
        // 200 이 아니라 400 으로 응답코드가 지정되도록
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    public boolean checkLike( Board board){
        SiteUser user =getUser();
        if(board.getLike().contains(user)){
            return true;

        }else {
            return false;
        }
    }
    public String redirect(String url, String msg) {
        return "redirect:" + Ut.url.modifyQueryParam(url, "msg", Ut.url.encodeWithTtl(msg));
    }
}





















