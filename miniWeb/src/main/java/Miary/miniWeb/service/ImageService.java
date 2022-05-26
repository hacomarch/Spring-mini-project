package Miary.miniWeb.service;

import Miary.miniWeb.diary.Diary;
import Miary.miniWeb.diary.DiaryRepository;
import Miary.miniWeb.diary.image.Image;
import Miary.miniWeb.diary.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ImageService {

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    DiaryService diaryService;

    @Value("${file.dir}")
    private String fileDir;

    public List<Image> findByDiary(Diary diary) {
        return imageRepository.findDiary(diary);
    }

    public List<Image> findAll(){
        return imageRepository.findAll();
    }

    @Transactional
    public Long deleteDiaryImage(Diary diary) {
        List<Image> image = imageRepository.findDiary(diary);
        if (!image.isEmpty()) {
            for (int i = 0; i < image.size(); i++) {
                imageRepository.delete(image.get(i));
                //image.remove(i);
            }
        }
        return diary.getDiaryIdx();
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Transactional
    @Modifying
    public List<Image> update(List<MultipartFile> multipartFiles, Diary diary) throws IOException {
        List<Image> diary1 = imageRepository.findDiary(diary);

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                diary1.add(storeFile(multipartFile, diary));
            }
        }
        return diary1;
    }

    @Transactional
    @Modifying
    public List<Image> storeFiles(List<MultipartFile> multipartFiles, Diary diary) throws IOException {
        List<Image> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, diary));
            }
        }
        return storeFileResult;
    }

    @Transactional
    @Modifying
    private Image storeFile(MultipartFile multipartFile, Diary diary) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        Image image = new Image();
        image.setUploadFileName(originalFilename);
        image.setStoreFileName(storeFileName);
        diaryRepository.saveAndFlush(diary);
        image.setDiary(diary);

        imageRepository.saveAndFlush(image);

        return image;
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;

    }

    //확장자를 별도로 추출해 서버 내부에서 관리하는 파일명에도 붙여준다.
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
