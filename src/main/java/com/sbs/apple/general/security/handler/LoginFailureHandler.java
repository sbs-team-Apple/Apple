package com.sbs.apple.general.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorType = "AuthenticationFailureType";

        if(exception instanceof BadCredentialsException){
            errorType = "password";
        }

        // UsernameNotFoundException
        if(exception instanceof UsernameNotFoundException){
            errorType = "username";
        }


        // forwarding with request attribute: exceptionMsg..
        response.sendRedirect(request.getContextPath() + "/login?error=true&type=" + errorType);
    }
}