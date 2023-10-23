package com.sbs.apple.board;


import com.sbs.apple.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    private String filename;
    private String filepath;

    private String subject;
    private String content;

    @ManyToOne
    private SiteUser siteUser;



}
