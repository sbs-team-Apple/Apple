package com.sbs.apple.board;

import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board create(MultipartFile file,String content, String subject, SiteUser user)
            throws Exception {
        Board board =new Board();


        String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName =uuid + "_" + file.getOriginalFilename();
        File saveFile =new File(projectPath,fileName);
        file.transferTo(saveFile);
        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);
        board.setSubject(subject);
        board.setContent(content);
        board.setSiteUser(user);
        this.boardRepository.save(board);
        return board;
    }

    public List<Board> getAllBoard(){

        return this.boardRepository.findAll();
    }

    public Board getBoard(Integer id){
        Optional<Board>  board =this.boardRepository.findById(id);
        if(board.isPresent()){
            return board.get();
        }else return null;
    }


    public Board modify(MultipartFile file, String subject, String content,Board board)
            throws Exception {



        String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName =uuid + "_" + file.getOriginalFilename();
        File saveFile =new File(projectPath,fileName);
        file.transferTo(saveFile);
        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);
        board.setSubject(subject);
        board.setContent(content);

        this.boardRepository.save(board);
        return board;
    }
}
