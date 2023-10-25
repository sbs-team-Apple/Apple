package com.sbs.apple.board;


import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.Banner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final UserService userService;
    private final BoardService boardService;


    @GetMapping("/create")
    public String Create(BoardForm boardForm) {
        return "/Board/appeal_board_create";

    }


    @PostMapping("/create")
    public String Create(@Valid BoardForm boardForm , MultipartFile file, Model model, Principal principal)throws Exception{
        SiteUser user = userService.getUserbyName(principal.getName());

        Board board= boardService.create(boardForm.getFile(),boardForm.getSubject(),boardForm.getContent(),user
              );


        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String showList(Model model) {
        System.out.println(System.currentTimeMillis());
        List<Board> boards =this.boardService.getAllBoard();

        model.addAttribute("board", boards);


        return "/Board/appeal_board_list";

    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Integer id, Model model) {

        Board board =this.boardService.getBoard(id);

        model.addAttribute("board", board);


        return "/Board/appeal_board_detail";

    }

    @GetMapping("/modify/{id}")
    public String modify2( BoardForm boardForm , MultipartFile file, Model model, Principal principal,@PathVariable Integer id){
       Board board = boardService.getBoard(id);
       model.addAttribute("board",board);

        return "/Board/appeal_board_modify";

    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid BoardForm boardForm , MultipartFile file, Model model, Principal principal,
                         @PathVariable Integer id)throws Exception{
        SiteUser user = userService.getUserbyName(principal.getName());
        Board board = boardService.getBoard(id);
        boardService.modify( boardForm.getFile(),boardForm.getSubject(), boardForm.getContent(), board);



        return "redirect:/board/list";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        Board board = boardService.getBoard(id);
        boardService.doDelete(board);


        return "redirect:/board/list";
    }




}
