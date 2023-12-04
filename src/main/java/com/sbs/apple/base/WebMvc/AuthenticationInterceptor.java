package com.sbs.apple.base.WebMvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getUserPrincipal() == null) {
            response.sendRedirect("/");
            return false; // Stop further processing
        }
        return true; // Continue with the request
    }

//     Other overridden methods if needed



}