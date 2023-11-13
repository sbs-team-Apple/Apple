package com.sbs.apple.interest;

import com.sbs.apple.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    //관심 주는 사람
    @ManyToOne
    private SiteUser siteUser;
    //관심 받는 사람
    @ManyToOne
    private SiteUser receivedSiteUser;

}