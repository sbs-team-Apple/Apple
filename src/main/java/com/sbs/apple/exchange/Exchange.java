package com.sbs.apple.exchange;

import com.sbs.apple.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;
    @ManyToOne
    private SiteUser siteUser;

    private String realname;

    private String email;

    private String address;

    private String f_No;

    private String phonNo_2;

    private String phonNo_3;

    private String homeAdress;

    private String nationality;

    private String bank;

    private String accountHolder;

    private String accountNumber;


}