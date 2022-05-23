package Miary.miniWeb.MemberManager.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long>, CustomProfileRepository {
    public List<Profile> findByNickname(String nickname);
}
