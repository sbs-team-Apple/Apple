package com.sbs.apple.imgs;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbs.apple.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImgsService {
    private final ImgsRepository imgsRepository;
    private String uploadDir;

    @Value("${file.upload-dir}")
        public void setUploadDir(String uploadDir) {
            this.uploadDir = uploadDir;
        }

    public  void create(List<MultipartFile> file, Board board)  throws Exception {
        File directory = new File(uploadDir);


        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }


        for(int i = 0; i < file.size(); i++) {
            Imgs imgs =new Imgs();
            imgs.setBoard(board);

            UUID uuid = UUID.randomUUID();
            String fileName =uuid + "_" + file.get(i).getOriginalFilename();
            File saveFile =new File(directory,fileName);
            file.get(i).transferTo(saveFile);
            imgs.setFilename(fileName);
            imgs.setFilepath("/gen/"+fileName);
            imgs.setIndexA(i+1);
            this.imgsRepository.save(imgs);

        }



    }

    public List<Imgs> getImg(Integer id){
        if(imgsRepository.findByBoardId(id) == null ){
            return null;
        }
        return imgsRepository.findByBoardId(id);


    }

    public List<Integer> getCurrentIndex(String myArray) {
        // ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 List<Integer>로 변환
            List<Integer> myList = objectMapper.readValue(myArray, new TypeReference<List<Integer>>() {
            });

            // 변환된 리스트 사용
            for (Integer number : myList) {
                System.out.println(number);
            }
            return myList;
            // 추가적인 로직 구현
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return null;
        }

    }
}
