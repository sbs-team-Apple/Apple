package com.sbs.apple.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class BoardForm {


    private MultipartFile file;



    private String content;


    private String subject;



}
