package com.sbs.apple.general.security.principal;

import com.sbs.apple.domain.user.SiteUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class UserContext extends org.springframework.security.core.userdetails.User {

    private final SiteUser siteUser;

    public UserContext(SiteUser siteUser, Collection<? extends GrantedAuthority> authorities) {
        super(siteUser.getUsername(), siteUser.getPassword(), authorities);
        this.siteUser = siteUser;
    }
}