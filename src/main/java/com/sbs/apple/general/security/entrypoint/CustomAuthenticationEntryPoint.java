package com.sbs.apple.general.security.entrypoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public CustomAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    //인증 실패시 처리하는 커스텀 인증 진입점('CustomAuthenticationEntryPoint')을 정의한 클래스. 실패시 어케 처리할 지
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // request : 웹 요청정보, response : 웹 응답정보, authException : 인증과정에서 발생한 예외객체.
        String errorType;
        String redirectUri = request.getRequestURI();
        String queryString;


        // UsernameNotFoundException
        if(authException instanceof UsernameNotFoundException) { // 사용자 이름 찾을수 없을 때

            errorType = "username";
            queryString = "?error=true&type=" + errorType;

            // redirect
            response.sendRedirect(redirectUri + queryString);
            //오류유형과 함께 url에 전달.클라이언트 해당 url로 리다이렉션

            // BadCredentialsException
        } else if(authException instanceof BadCredentialsException) { //비밀번호 인증 실패

            errorType = "password";
            queryString = "?error=true&type=" + errorType;

            // redirect : 클라이언트를 다른 URL로 이동
            // 목적: 클라이언트의 브라우저를 다른 url로 이동
            // 동작방식 : 서버가 클라이언트에게 새로운 url로 이동하도록 응답.
            // 새로운 요청 발생. 클라이언트와 서버간 새로운 상태 가짐.
            response.sendRedirect(redirectUri + queryString);


        } else if(authException instanceof InsufficientAuthenticationException) { // anonymous가 authenticated에 접근

            request.getSession().setAttribute("msg", "로그인 후 이용해주세요");

            // forward : 현재 서버에서 요청을 다른 리소스로 전달
            // 목적: 현재 실행중인 서블릿이나 JSP에서 다른 서블린,JSP로 넘기는 것
            // 동작 방식: 서블릿이나 JSP에서 'forward' 메서드 또는 태그를 사용 다른 서블릿,JSP로 요청 전달
            // 클라이언트는 이러한 전환을 인지하지 못하며, 모든 작업은 서버에서 이루어 짐.
            // 새로운 요청 발생하지 않음
            super.commence(request, response, authException);
        }
    }
}