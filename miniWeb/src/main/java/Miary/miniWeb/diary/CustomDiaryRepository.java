package Miary.miniWeb.diary;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomDiaryRepository {

    public Diary findById(Long diary_idx);
    public List<Diary> findByMemberId(Long memberIdx);
    public List<Diary> findByTitle(String title);
    public int deleteDiary(Long diary_idx);
}
