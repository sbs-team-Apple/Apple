package com.sbs.apple.user;

import com.sbs.apple.report.Report;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //회원가입 할 때 기본 정보
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String nickname;
    private String gender; //성별
    //기본 프로필 기입
    private int age;//나이

    private String living;//사는 지역

    private String hobby;//취미 여려개 추가할 수 있게 변경하기

    private int tall; //키

    private String body_type; //체형

    private boolean smoking; //흡연 유무
    private String drinking; //음주 유무
    private String style; //스타일(성격)
    private String religion; //종교
    private String mbti; //MBTI
    private String school; //학력
    private String job; //직장
    private String About_Me; //자기소개

    private String desired_age; //원하는 나이 어떻게 구현할지 다시 고민
    private String desired_living; //원하는 지역
    private String desired_hobby; // 여러개 고를 수 있게 하기
    private String desired_tall; // 원하는 키 어떻게 범위 설정할지 다시 고민
    private String desired_body_type; //원하는 체형
    private String desired_smoking; // 원하는 흡연 유무
    private String desired_drinking; // 원하는 음주 유무
    private String desired_style; //원하는 스타일
    private String desired_religion; //원하는 종교
    private String desired_mbti; //원하는 MBTI

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Report> reportList;

    @Column
    private Integer cyberMoney = 0; // 기본값 0으로 초기화


    public Integer getCyberMoney() {
        if (cyberMoney == null) {
            return 0; // 필드가 null인 경우 0을 반환
        }
        return cyberMoney;
    }
    //여러개를 선택해야할 때의 칼럼
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("siteuser"));
        if ("admin".equals(username)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }
        return grantedAuthorities;
    }
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserRole> authorities;
    public boolean hasRole(UserRole role) {
        return authorities.contains(role);
    }

    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }
}
