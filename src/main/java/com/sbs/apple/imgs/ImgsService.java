package com.sbs.apple.imgs;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbs.apple.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
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


    //수정에서 사진추가용 크레이트
    public  void create2(List<MultipartFile> file, Board board , List<Integer> addIndex)  throws Exception {
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
            imgs.setIndexA(addIndex.get(i));
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

            return myList;
            // 추가적인 로직 구현
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return null;
        }

    }

    public List<Imgs> modifyImgIndex(List<Imgs> imgs, List<Integer> currentIndex, Board board) {

        List<Integer> imgIndex=new ArrayList<>();

        if(!imgIndex.equals(currentIndex)){
            System.out.println("수정사항 있음");


            outerLoop:
            for (int i = 0; i < currentIndex.size(); i++) {
                for (int j = 0; j < currentIndex.size(); j++) {
                    if (imgs.get(i).getIndexA()==currentIndex.get(j)){
                        imgs.get(i).setIndexA(j+1);
                        imgsRepository.save(imgs.get(i));

                        continue outerLoop; // Continue from the next iteration of the outer loop
                    }
                }

            }
        }
        return imgs;
    }



    public List<Imgs> getImgsByBoard(Board board){
        List<Imgs> imgs = imgsRepository.findByBoardIdOrderByIndexAAsc(board.getId());
        if(imgs==null){
            return null;
        }
        return imgs;

    }


    //indexA 로 이미지 찾기 현재 순서대로 찾은다음에 수정된 번호 대입시켜야 되서
    public Imgs getImgsByIndexA(Integer num, Board board){
        Imgs img = imgsRepository.findByBoardIdAndIndexA(board,num);

        return img;
    }

    public void deleteImgs(Board board,List<Integer> deleteIndex  ){
        imgsRepository.deleteByBoardAndIndexA(board, deleteIndex);

    }
}
