package com.sbs.apple.general.security.service;

import com.sbs.apple.domain.user.SiteUser;
import com.sbs.apple.domain.user.UserRepository;
import com.sbs.apple.general.security.principal.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<SiteUser> optionalSiteUser = userRepository.findByUsername(username);

        // username not found check
        SiteUser siteUser = optionalSiteUser.orElseThrow(() -> new UsernameNotFoundException("UsernameNotFoundException"));

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + siteUser.getAuthorities()));

        UserContext userContext = new UserContext(siteUser, roles);

        return userContext;
    }
}
