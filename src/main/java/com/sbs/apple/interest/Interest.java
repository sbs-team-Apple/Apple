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

    private String interestUser;
    private String interestedUser;

    @ManyToOne
    private SiteUser siteUser;
}
