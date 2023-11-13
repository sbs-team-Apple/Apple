package com.sbs.apple.general.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationProvider customAuthenticationProvider;
    private final AccessDeniedHandler defaultAccessDeniedHandler;
    private final AuthenticationFailureHandler loginFailureHandler;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .authorizeHttpRequests(auth -> auth

                        // 메인, 접근거부, 유효하지 않은 요청, error
                        .requestMatchers("/", "/accessDenied*", "/unexpected_request*", "/error*", "/gen*", "/gen/**").permitAll()

                        // 회원가입, 로그인, 관리자 권한 부여
                        .requestMatchers("/signup*", "/add/**", "/desired/**", "/user/login*", "/admin/grantAuthority").permitAll()

                        // 관심목록
                        .requestMatchers("/wishlist/add").hasRole("USER")

                        // admin 제한
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/")
                        .failureHandler(loginFailureHandler)
                        .permitAll()
                )

                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/user/login"))

                .csrf(AbstractHttpConfigurer::disable)

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(defaultAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );



        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring().requestMatchers("/css/**", "/js/**");
    }

}



