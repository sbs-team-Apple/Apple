package com.sbs.apple.board;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class ImgsService {
    private ImgsRepository imgsRepository;
    private String uploadDir;

    @Value("${file.upload-dir}")
        public void setUploadDir(String uploadDir) {
            this.uploadDir = uploadDir;
        }

    public  void create(List<MultipartFile> file)  throws Exception {
        File directory = new File(uploadDir);
        Imgs imgs =new Imgs();
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }


        for(int i = 0; i < file.size(); i++) {
            UUID uuid = UUID.randomUUID();
            String fileName =uuid + "_" + file.get(i).getOriginalFilename();
            File saveFile =new File(directory,fileName);
            file.get(i).transferTo(saveFile);
            imgs.setFilename(fileName);
            imgs.setFilepath("/gen/"+fileName);
            this.imgsRepository.save(imgs);

        }



    }

}
