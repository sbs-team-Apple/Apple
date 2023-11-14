package com.sbs.apple.board;

import com.sbs.apple.imgs.Imgs;
import com.sbs.apple.imgs.ImgsService;
import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
    private String uploadDir;
    private final ImgsService imgsService;

    @Value("${file.upload-dir}")
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }



    public Board create(List<MultipartFile> file,String subject,String content,  SiteUser user)
            throws Exception {
        Board board =new Board();
        Imgs imgs =new Imgs();

        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }


        board.setSubject(subject);
        board.setContent(content);
        board.setSiteUser(user);


        UUID uuid = UUID.randomUUID();
        if(file.get(0).getOriginalFilename().equals("")){
            System.out.println("사진없음");
//            board.setFilename("곰.jfif");
//            board.setFilepath("/img/곰.jfif");

            this.boardRepository.save(board);
            return board;


        }



//        board.setFilename(fileName);
//        board.setFilepath("/gen/"+fileName);

        this.boardRepository.save(board);
        this.imgsService.create(file,board);
        return board;
    }

    public List<Board> getAllBoard(){

        return this.boardRepository.findAll();
    }


    public List<Board> getAllBoardDESC(){

        return boardRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }



    public Board getBoard(Integer id){
        Optional<Board> boards =this.boardRepository.findById(id);
        if(boards.isPresent()){
            return boards.get();
        }else return null;
    }


    public Board modify(MultipartFile file, String subject, String content,Board board)
            throws Exception {
        if(file.isEmpty()){
            board.setSubject(subject);
            board.setContent(content);

            this.boardRepository.save(board);
            return board;

        }

        File directory = new File(uploadDir);

        UUID uuid = UUID.randomUUID();
        String fileName =uuid + "_" + file.getOriginalFilename();
        File saveFile =new File(directory,fileName);
        file.transferTo(saveFile);
//        board.setFilename(fileName);
//        board.setFilepath("/gen/"+fileName);
        board.setSubject(subject);
        board.setContent(content);

        this.boardRepository.save(board);
        return board;
    }

    public void doDelete(Board board) {
        this.boardRepository.delete(board);

    }

    public List<Board> getBoardByUserId(SiteUser user) {
        List<Board> boards=this.boardRepository.findByUserId(user);
        if(boards==null){
            return null;

        }
        return boards;
    }
}
