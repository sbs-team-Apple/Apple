package com.sbs.apple.board;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
public class BoardForm {

    private List<MultipartFile> file;



    private String content;





}
