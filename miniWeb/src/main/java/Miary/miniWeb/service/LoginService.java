package Miary.miniWeb.service;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public Member login(String id, String password) {
        return memberRepository.findByLoginId(id)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null); //위의 조건을 만족하지 않으면 null return
    }

}
