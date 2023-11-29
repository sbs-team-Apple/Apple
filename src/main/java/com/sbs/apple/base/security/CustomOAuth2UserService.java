package com.sbs.apple.base.security;

import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;
    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 로그인 후 기본 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자의 소셜 계정 고유 ID를 저장할 변수
        String oauthId;

        // 사용자의 소셜 계정 닉네임을 저장할 변수
        String nickname;

        // 사용자가 선택한 소셜 로그인 제공자의 종류를 저장할 변수 (예: NAVER, GOOGLE 등)
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        // 네이버 로그인을 선택한 사용자 정보 추출
        if ("NAVER".equals(providerTypeCode)) {
            Map<String, Object> naverAttributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            oauthId = (String) naverAttributes.get("id");

            // 구글 로그인을 선택한 사용자 정보 추출
        } else if ("GOOGLE".equals(providerTypeCode)) {
            oauthId = oAuth2User.getName();

            // 카카오 로그인을 선택한 사용자 정보 추출
        } else if ("KAKAO".equals(providerTypeCode)) {
            Map<String, Object> kakaoAttributes = (Map<String, Object>) oAuth2User.getAttributes();
            oauthId = String.valueOf(kakaoAttributes.get("id"));


            // 다른 소셜 로그인 서비스를 선택한 경우의 기본 처리
        } else {
            oauthId = oAuth2User.getName();
            nickname = (String) oAuth2User.getAttributes().get("nickname"); // 기본 처리
        }
        // DB에 저장될 유저네임 형식 생성
        String username = providerTypeCode + "__%s".formatted(oauthId);

        SiteUser siteUser =userService.whenSocialLogin(providerTypeCode, username);
        return new CustomOAuth2User(siteUser.getUsername(), siteUser.getPassword(), siteUser.getGrantedAuthorities());
    }
}

class CustomOAuth2User extends User implements OAuth2User {

    public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}