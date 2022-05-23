package Miary.miniWeb.service;

import Miary.miniWeb.MemberManager.profile.Profile;
import Miary.miniWeb.MemberManager.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Transactional
    public Long saveProfile(Profile profile) {
        profileRepository.save(profile);
        return profile.getProfileIdx();
    }

    public List<Profile> findAll(){
        return profileRepository.findAll();
    }
    public List<Profile> findByMemberId(Long memberIdx) {
        return profileRepository.findByMemberId(memberIdx);
    }

    public Profile findByProfileId(Long profileIdx) {
        return profileRepository.findOne(profileIdx);
    }

    @Transactional
    public Profile findByNickname(String nickname) {
        return findAll().stream().filter(m -> m.getNickname().equals(nickname)).findFirst().orElse(null);
    }

}
