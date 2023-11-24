package com.sbs.apple.board;


import com.sbs.apple.imgs.Imgs;
import com.sbs.apple.imgs.ImgsService;
import com.sbs.apple.user.SiteUser;
import com.sbs.apple.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final UserService userService;
    private final BoardService boardService;
    private final ImgsService imgsService;
    private final BoardRepository boardRepository;
    private Board board;


    @GetMapping("/create")
    public String Create(BoardForm boardForm,  @RequestParam("key-kind") String kind, Model model) {
        System.out.println(kind);
        model.addAttribute("keyKind", kind);
        return "board/appeal_board_create";

    }


    @PostMapping("/create")
    public String Create(@Valid BoardForm boardForm, MultipartFile file, Model model, Principal principal, @RequestParam("key-kind") String kind ) throws Exception {
        SiteUser user = userService.getUserbyName(principal.getName());
        if(boardForm.getFile()==null){


        }


        Board board = boardService.create(boardForm.getFile(),boardForm.getContent(), user);

        if(kind.equals("my")){
            return "redirect:/board/myAppealBoard";

        }else {

        return "redirect:/board/appealList";
         }

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
    public String modify2(BoardForm boardForm, MultipartFile file, Model model, Principal principal, @PathVariable Integer id, @RequestParam("key-kind") String kind) {
         Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        List<Imgs> imgs=imgsService.getImgsByBoard(board);
        System.out.println("현재 이미지 순서 !!!!!!!!!!!!!!!!!!");
        for (int i = 0; i < imgs.size(); i++) {
            System.out.println(imgs.get(i).getIndexA());
        }
        model.addAttribute("keyKind", kind);


        return "board/appeal_board_modify";

    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid BoardForm boardForm ,MultipartFile file, Model model, Principal principal,
                         @PathVariable Integer id,@RequestParam("myArray") String myArray,@RequestParam("myAddArray") String myAddArray, @RequestParam("key-kind") String kind)throws Exception {
        SiteUser user = userService.getUserbyName(principal.getName());
        Board board = boardService.getBoard(id);
        List<Imgs> imgs=imgsService.getImgsByBoard(board);

        //삭제할 번호를 둘 리스트에 일단 현재 이미지 인덱스 번호 넣기
        List<Integer> deleteIndex = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            deleteIndex.add(imgs.get(i).getIndexA());
        }


        System.out.println("이미지 크기 !!!!!!!!!!!!!!!!!" +imgs.size());
        System.out.println("현재 이미지 순서 !!!!!!!!!!!!!!!!!!");

        for (int i = 0; i < imgs.size(); i++) {
            System.out.println(imgs.get(i).getIndexA());
        }
        List<Integer> currentIndex= imgsService.getCurrentIndex(myArray);
        List<Integer> addIndex= imgsService.getCurrentIndex(myAddArray);

        System.out.println("받아온 순서랑 추가된 이미지 번호들 !!!!!!!!!!!!!!!!!");
        for (int i = 0; i < currentIndex.size(); i++) {
            System.out.println(currentIndex.get(i));
        }


        System.out.println("더헤야되는 인덱스 번호들 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        for (int i = 0; i < addIndex.size(); i++) {
            System.out.println(addIndex.get(i));
        }


        //현재 있던 번호에서 수정된 번호만큼 빼주기
        deleteIndex.removeAll(currentIndex);
        //더해야하는 인덱스번호도 지워지면 안되니까 빼주기
        deleteIndex.removeAll(addIndex);

        System.out.println("삭제할 인덱스 번호들!!!!!!!!!!!!!!!!");
        for (int i = 0; i < deleteIndex.size(); i++) {
            System.out.println(deleteIndex.get(i));
        }


        System.out.println("바껴진 순서 번호!!!!!!!!!!!!!!");
        for(int i = 0; i <currentIndex.size() ; i++) {
            System.out.println(currentIndex.get(i));
        }
        // 먼저 커렌트 인덱스번호에서 없는 것들 이미지 삭제 해주기


        imgsService.deleteImgs(board,deleteIndex);


        imgs=imgsService.getImgsByBoard(board);


        System.out.println("삭제후 남은 이미지 갯수 구하기 !!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(imgs.size());



        if (file != null && !file.isEmpty()) {
            Board board2 = boardService.create2(boardForm.getFile(),  boardForm.getContent(), user, addIndex, board);
        }


        //삭제되었으니까 남아있는걸로 다시 불러와주기
        imgs=imgsService.getImgsByBoard(board);


        //순서 변경해주기
        imgs=imgsService.modifyImgIndex(imgs,currentIndex,board);




        boardService.modify( boardForm.getContent(), board,imgs);


        if(kind.equals("my")){
            return "redirect:/board/myAppealBoard";

        }

        return "redirect:/board/appealList";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, @RequestParam("key-kind") String kind ) {
        Board board = boardService.getBoard(id);

        boardService.doDelete(board);
        System.out.println("삭제 실행됨~!!!!!!!!!!!!!!!!!!!!!!");
        if(kind.equals("my")){
            return "redirect:/board/myAppealBoard";
        }

        return "redirect:/board/appealList";
    }


    @GetMapping("/myAppealBoard")
    public String showMyBoard(Principal principal, Model model) {
        SiteUser user = userService.getUserbyName(principal.getName());
        List<Board> boards = boardService.getBoardByUserId(user);

        model.addAttribute("board", boards);

        return "board/my_appeal_board";
    }

    @GetMapping("/myAppealBoardList")
    public String showMyBoardList(Principal principal, Model model) {
        SiteUser user = userService.getUserbyName(principal.getName());
        List<Board> boards = boardService.getBoardByUserId(user);

        model.addAttribute("board", boards);

        return "board/my_appeal_board_list";
    }



    @GetMapping("/desiredList")
    public String showMyDreamUsers(Principal principal, Model model) {
        SiteUser user = userService.getUserbyName(principal.getName());
        List<SiteUser> users = userService.getDesiredUsers(user);


        model.addAttribute("users", users);


        return "board/my_desiredList";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like/{id}")
    public String boradLike(Principal principal, @PathVariable("id") Integer id) {
        board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if(board.getLike().contains(siteUser)){
            board.getLike().remove(siteUser);
            boardRepository.save(board);
            System.out.println("이미 하트 눌렀던 애 사라짐");
                    

        }else {
            this.boardService.like(board, siteUser);
            System.out.println("하트 누른 사람 추가");

        }
        return "board/appeal_board_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like/{id}")
    @ResponseBody
    public  Map<String, Integer>  boradLike2(Principal principal, @PathVariable("id") Integer id, Model model) {
        System.out.println("좋아요수 업데이트 해주기--------------------------");
        Map<String, Integer> response = new HashMap<>();
        response.put("likeCount", board.getLike().size()); // Add the like count to the response

        return response;
    }


}

