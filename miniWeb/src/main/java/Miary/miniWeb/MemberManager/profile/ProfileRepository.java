package Miary.miniWeb.MemberManager.profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long>, CustomProfileRepository {
}
