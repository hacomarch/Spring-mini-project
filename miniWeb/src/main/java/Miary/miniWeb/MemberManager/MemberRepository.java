package Miary.miniWeb.MemberManager;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    @PersistenceContext
    EntityManager em;

    public void save(Member member) {
        if (member.getMemberIdx() == null) {
            this.em.persist(member);
        }else{
            this.em.merge(member);
        }
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Member findOne(Long memberIdx){
        return em.find(Member.class, memberIdx);
    }

    public void destroyMember(Member member){
        em.remove(member);
    }

    public Optional<Member> findByLoginId(String loginId){
        return findAll().stream().filter(m -> m.getId().equals(loginId)).findFirst();
    }



}
