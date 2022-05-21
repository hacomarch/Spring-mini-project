package Miary.miniWeb.MemberManager.profile.profileImage;

import Miary.miniWeb.MemberManager.profile.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomProfileImageRepositoryImpl implements CustomProfileImageRepository{
    @PersistenceContext
    EntityManager em;

    @Override
    public List<ProfileImage> findProfile(Profile profile) {
        return em.createQuery("select pi from ProfileImage pi where pi.profileImage = :profile", ProfileImage.class).setParameter("profile", profile).getResultList();
    }
}
