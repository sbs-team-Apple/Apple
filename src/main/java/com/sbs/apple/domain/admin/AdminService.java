package com.sbs.apple.domain.admin;

import com.sbs.apple.domain.user.SiteUser;
import com.sbs.apple.general.security.principal.UserContext;
import com.sbs.apple.util.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final Rq rq;

    public boolean isAdmin() {
        return rq.isAdmin();
    }

    public void grantAdminAuthority(SiteUser siteUser) {

        // 새로운 GrantedAuthority List 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UserContext userContext = new UserContext(siteUser, authorities);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(siteUser, null, userContext.getAuthorities());


        // authentication 토큰 갱신
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

    public void revokeAdminAuthority(SiteUser siteUser) {
        // 새로운 GrantedAuthority List 생성
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserContext userContext = new UserContext(siteUser, authorities);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(siteUser, null, userContext.getAuthorities());


        // authentication 토큰 갱신
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}