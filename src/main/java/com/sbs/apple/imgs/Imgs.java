package com.sbs.apple.imgs;


import com.sbs.apple.board.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Imgs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @ManyToOne
    private Board board;



    private String filename;
    private String filepath;




}
