package com.sbs.apple.board;


import com.sbs.apple.imgs.Imgs;
import com.sbs.apple.imgs.ImgsService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final UserService userService;
    private final BoardService boardService;
    private final ImgsService imgsService;


    @GetMapping("/create")
    public String Create(BoardForm boardForm) {
        return "board/appeal_board_create";

    }


    @PostMapping("/create")
    public String Create(@Valid BoardForm boardForm, MultipartFile file, Model model, Principal principal) throws Exception {
        SiteUser user = userService.getUserbyName(principal.getName());
        if(boardForm.getFile()==null){


        }


        Board board = boardService.create(boardForm.getFile(), boardForm.getSubject(), boardForm.getContent(), user
        );


        return "redirect:/board/appealList";
    }

    @GetMapping("/appealList")
    public String showList(Model model) {
        System.out.println(System.currentTimeMillis());

        List<Board> boards =this.boardService.getAllBoardDESC();


        model.addAttribute("board", boards);


        return "board/appeal_board_list";

    }

//    @GetMapping("/detail/{id}")
//    public String showDetail(@PathVariable Integer id, Model model) {
//
//        Board board = this.boardService.getBoard(id);
//
//        model.addAttribute("board", board);
//
//
//        return "board/appeal_board_detail";
//
//    }

    @GetMapping("/modify/{id}")
    public String modify2(BoardForm boardForm, MultipartFile file, Model model, Principal principal, @PathVariable Integer id) {
         Board board = boardService.getBoard(id);
        model.addAttribute("board", board);


        return "board/appeal_board_modify";

    }

    @PostMapping("/modify/{id}")
    public String modify( Model model, Principal principal,
                         @PathVariable Integer id,@RequestParam("myArray") String myArray)throws Exception {
        SiteUser user = userService.getUserbyName(principal.getName());
//        Board board = boardService.getBoard(id);
        List<Imgs> imgs=imgsService.getImg(id);
        List<Integer> currentIndex= imgsService.getCurrentIndex(myArray);
        System.out.println("인덱스 리스트의 크기 "+currentIndex.size());
        for(int i = 0; i <currentIndex.size() ; i++) {
            System.out.println(currentIndex.get(i));
        }
        System.out.println("끝!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

//        boardService.modify( boardForm.getFile(),boardForm.getSubject(), boardForm.getContent(), board);




        return "redirect:/board/appealList";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
//        Board board = boardService.getBoard(id);
//        boardService.doDelete(board);


        return "redirect:/board/appealList";
    }


    @GetMapping("/myAppealBoard")
    public String showMyBoard(Principal principal, Model model) {
        SiteUser user = userService.getUserbyName(principal.getName());
        List<Board> boards = boardService.getBoardByUserId(user);

        model.addAttribute("board", boards);


        return "board/my_appeal_board";
    }


    @GetMapping("/desiredList")
    public String showMyDreamUsers(Principal principal, Model model) {
        SiteUser user = userService.getUserbyName(principal.getName());
        List<SiteUser> users = userService.getDesiredUsers(user);


        model.addAttribute("users", users);


        return "board/my_desiredList";
    }


}

