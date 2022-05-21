package Miary.miniWeb.diary.image;

import Miary.miniWeb.diary.Diary;

import java.util.List;

public interface CustomImageRepository {
    public int deleteImage(Diary diary);

    List<Image> findDiary(Diary diary);

}
