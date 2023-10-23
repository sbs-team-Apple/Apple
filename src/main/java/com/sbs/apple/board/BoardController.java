package com.sbs.apple.board;


import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Controller
public class BoardController {
    private final UserService userService;
    private final BoardService boardService;


    @GetMapping("/board/create")
    public String Create2(BoardForm boardForm) {
        return "/Board/appeal_board_create";

    }


    @PostMapping("/board/create2")
    public String Create(@Valid BoardForm boardForm , MultipartFile file, Model model, Principal principal)throws Exception{
        SiteUser user = userService.getUserbyName(principal.getName());

        Board board= boardService.create(boardForm.getFile(),boardForm.getSubject(),boardForm.getContent(),user
              );


        return "redirect:/list";
    }

    @GetMapping("/board/list")
    public String showList(Model model) {

        List<Board> boards =this.boardService.getAllBoard();

        model.addAttribute("board", boards);


        return "/Board/appeal_board_list";

    }

    @GetMapping("/board/detail/{id}")
    public String showDetail(@PathVariable Integer id, Model model) {

        Board board =this.boardService.getBoard(id);

        model.addAttribute("board", board);


        return "/Board/appeal_board_detail";

    }






}
