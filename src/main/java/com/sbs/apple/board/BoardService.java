package com.sbs.apple.board;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbs.apple.imgs.Imgs;
import com.sbs.apple.imgs.ImgsService;
import com.sbs.apple.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
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


    public Board create(List<MultipartFile> file,  String content, SiteUser user)
            throws Exception {
        Board board = new Board();

        board.setContent(content);
        board.setSiteUser(user);
        board.setCreateDate(LocalDateTime.now());


        this.boardRepository.save(board);
        this.imgsService.create(file, board);
        return board;
    }


    //수정 할때 추가되는 이미지 용 크레이트 함수
    public Board create2(List<MultipartFile> file,  String content, SiteUser user, List<Integer> addIndex, Board board)
            throws Exception {


        this.imgsService.create2(file, board, addIndex);
        return board;
    }

    public List<Board> getAllBoard() {

        return this.boardRepository.findAll();
    }


    public List<Board> getAllBoardDESC() {

        return boardRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }


    public Board getBoard(Integer id) {
        Optional<Board> boards = this.boardRepository.findById(id);
        if (boards.isPresent()) {
            return boards.get();
        } else return null;
    }


    public Board modify(  String content, Board board, List<Imgs> imgs)
            throws Exception {
//        if (file.isEmpty()) {
//            board.setSubject(subject);
//            board.setContent(content);
//
//            this.boardRepository.save(board);
//            return board;
//
//        }


        board.setContent(content);
        board.setImgs(imgs);
        this.boardRepository.save(board);
        return board;
    }

    public void doDelete(Board board) {
        this.boardRepository.delete(board);

    }

    public List<Board> getBoardByUserId(SiteUser user) {
        List<Board> boards = this.boardRepository.findByUserId(user);
        if (boards == null) {
            return null;

        }
        return boards;
    }


    public void like(Board board, SiteUser siteUser) {
        board.getLike().add(siteUser);
        this.boardRepository.save(board);
    }


}
