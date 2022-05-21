package Miary.miniWeb.service;


import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    @Autowired MemberRepository memberRepository;

    //회원 가입
    @Transactional
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getMemberIdx();
    }

    @Transactional
    public Long update(Member member){
        memberRepository.save(member);
        return member.getMemberIdx();
    }

    //전체 회원 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findMemberIdx(Long memberIdx){
        return memberRepository.findOne(memberIdx);
    }

    @Transactional
    public Long destroy(Member member){
        memberRepository.destroyMember(member);
        return member.getMemberIdx();
    }


}
