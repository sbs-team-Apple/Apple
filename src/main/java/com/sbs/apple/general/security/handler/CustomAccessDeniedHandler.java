package com.sbs.apple.general.security.handler;

import com.sbs.apple.util.Rq;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Rq rq;

    @Setter
    private String defaultAccessDeniedPageUrl = "/accessDenied";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {


        // 관리자 권한 메세지
        if(request.getRequestURI().startsWith("/admin")) {
            response.sendRedirect("/accessDenied?requiredAuthority=admin");
        }
        // 기본 메세지
        else {
            request.setAttribute("msg", accessDeniedException.getMessage());
            request.getRequestDispatcher(defaultAccessDeniedPageUrl).forward(request, response);
        }
    }
}