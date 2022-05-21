package Miary.miniWeb.diary;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomDiaryRepositoryImpl implements CustomDiaryRepository{

    @PersistenceContext
    EntityManager em;


    public Diary findById(Long diary_idx) {
        return em.find(Diary.class, diary_idx);
    }

    public List<Diary> findByMemberId(Long memberIdx){
        return em.createQuery("select d from Diary d left join d.member m where m.memberIdx = :memberIdx", Diary.class).setParameter("memberIdx", memberIdx).getResultList();
    }

    public List<Diary> findByTitle(String title) {
        return em.createQuery("select d from Diary d where d.title = :title", Diary.class).setParameter("title", title).getResultList();
    }

    @Modifying
    @Transactional
    public int deleteDiary(Long diaryIdx) {
        return em.createQuery("DELETE FROM Diary AS d where d.diaryIdx=:diaryIdx").setParameter("diaryIdx", diaryIdx).executeUpdate();
    }


}
