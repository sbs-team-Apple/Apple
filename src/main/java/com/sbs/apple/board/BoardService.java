package com.sbs.apple.board;

import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.upload-dir}")
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }



    public Board create(MultipartFile file,String subject,String content,  SiteUser user)
            throws Exception {
        Board board =new Board();


        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }
        UUID uuid = UUID.randomUUID();
        String fileName =uuid + "_" + file.getOriginalFilename();
        File saveFile =new File(directory,fileName);
        file.transferTo(saveFile);
        board.setFilename(fileName);
        board.setFilepath("/gen/"+fileName);
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


        File directory = new File(uploadDir);

        UUID uuid = UUID.randomUUID();
        String fileName =uuid + "_" + file.getOriginalFilename();
        File saveFile =new File(directory,fileName);
        file.transferTo(saveFile);
        board.setFilename(fileName);
        board.setFilepath("/gen/"+fileName);
        board.setSubject(subject);
        board.setContent(content);

        this.boardRepository.save(board);
        return board;
    }
}
