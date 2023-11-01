package com.sbs.apple.notification;


import com.sbs.apple.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;



@Entity
@Setter
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String kind;

    @ManyToOne
    private SiteUser siteUser;


    @ManyToOne
    private SiteUser siteUserFrom;



    private LocalDateTime createDate;




}
