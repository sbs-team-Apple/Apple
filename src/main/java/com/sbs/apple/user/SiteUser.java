package com.sbs.apple.user;


import com.sbs.apple.board.Board;
import com.sbs.apple.chat.ChatRoom;
import com.sbs.apple.cybermoney.CyberMoneyTransaction;
import com.sbs.apple.interest.Interest;
import com.sbs.apple.notification.Notification;
import com.sbs.apple.report.Report;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PUBLIC;

@Getter
@Setter
@Entity
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PUBLIC)
@SuperBuilder
@ToString(callSuper = true)
public class SiteUser {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = true)
    private boolean userStop;

    private boolean userWarning;
    private String filename;
    private String filepath;
    //회원가입 할 때 기본 정보
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String nickname;
    private String gender; //성별
    //기본 프로필 기입
    private int age;//나이
    private String email;
    private String living;//사는 지역

    private List<String> hobbyList;
    //private String[] hobby; //취미 여려개 추가할 수 있게 변경하기

    private float tall; //키

    private String body_type; //체형

    @Column(nullable = true)
    private String smoking; //흡연 유무

    private String drinking; //음주 유무
    private List<String> styleList;//스타일 (성격)
    private String religion; //종교
    private String mbti; //MBTI
    private String school; //학력
    private String job; //직장
    private String About_Me; //자기소개

    private String desired_age;
//    private int desired_age1;
//    private int desired_age2;
    private String desired_hobby; // 여러개 고를 수 있게 하기
    private String desired_living; //원하는 지역
    private String desired_tall; // 원하는 키 어떻게 범위 설정할지 다시 고민
    private String desired_body_type; //원하는 체형
    private String desired_smoking; // 원하는 흡연 유무
    private String desired_drinking; // 원하는 음주 유무
    private List<String>  desired_styleList;//원하는 스타일 리스트
    private String desired_religion; //원하는 종교
    private String desired_mbti; //원하는 MBTI



    @OneToMany(mappedBy = "siteUser" , cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRoomList;


    @OneToMany(mappedBy = "siteUser" , cascade = CascadeType.REMOVE)
    private List<Notification> notificationList;







    private String desired_school;
    private String desired_job;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Interest> interestList;

    @OneToMany(mappedBy = "siteUser", cascade = CascadeType.REMOVE)
    private List<Report> reportList;


    private int cyberMoney = 5000; // 기본값 0으로 초기화

    @Column
    private Integer receivedCyberMoney = 5000; // 다른 사용자로부터 받은 사이버머니 기본값 0으로 초기화
    public Integer getReceivedCyberMoney() {
        if (receivedCyberMoney == null) {
            return 0; // 또는 다른 기본값을 사용할 수 있습니다.
        }
        return receivedCyberMoney;
    }

    //여러개를 선택해야할 때의 칼럼
    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("siteuser"));
        grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        return grantedAuthorities;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> authorities = new HashSet<>();

    public boolean hasRole(UserRole role) {
        return authorities.contains(role);
    }

    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }



    @OneToMany(mappedBy = "siteUser" , cascade = CascadeType.REMOVE)
    private List<Board> boardList;


    @Getter
    @OneToMany(mappedBy = "recipientUser", fetch = FetchType.LAZY)
    private List<CyberMoneyTransaction> receivedTransactions;

    public List<CyberMoneyTransaction> getCompletedTransactions() {
        List<CyberMoneyTransaction> completedTransactions = new ArrayList<>();

        // 여기에서 완료된 거래 목록을 생성하고 반환합니다.
        for (CyberMoneyTransaction transaction : receivedTransactions) {
            if (transaction.isAccepted() || transaction.isRejected()) {
                completedTransactions.add(transaction);
            }
        }

        return completedTransactions;
    }

    @Column
    private Integer minHeart = 200;

}