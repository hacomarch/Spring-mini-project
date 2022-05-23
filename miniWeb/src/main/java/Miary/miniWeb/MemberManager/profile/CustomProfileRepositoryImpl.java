package Miary.miniWeb.MemberManager.profile;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomProfileRepositoryImpl implements CustomProfileRepository{
    @PersistenceContext
    EntityManager em;


    @Override
    public List<Profile> findByMemberId(Long memberIdx) {
        return em.createQuery("select p from Profile p left join p.memberProfile m where m.memberIdx = :memberIdx", Profile.class).setParameter("memberIdx", memberIdx).getResultList();
    }

    @Override
    public Profile findOne(Long profileIdx) {
        return em.find(Profile.class, profileIdx);
    }

}
