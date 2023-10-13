package com.sbs.apple.chat;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //회원가입 할 때 기본 정보

    @Column
    private Integer userId1;

    @Column
    private Integer userId2;

    private LocalDateTime createDate;
}
