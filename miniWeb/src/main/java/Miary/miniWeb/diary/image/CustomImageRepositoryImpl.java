package Miary.miniWeb.diary.image;

import Miary.miniWeb.diary.Diary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomImageRepositoryImpl implements CustomImageRepository{

    @PersistenceContext
    EntityManager em;

    @Modifying
    @Transactional
    public int deleteImage(Diary diary) {
        return em.createQuery("DELETE FROM Image AS i where i.diary=:diary").setParameter("diary", diary).executeUpdate();
    }

    @Override
    public List<Image> findDiary(Diary diary) {
        return em.createQuery("select i from Image i where i.diary = :diary", Image.class).setParameter("diary", diary).getResultList();
    }
}
