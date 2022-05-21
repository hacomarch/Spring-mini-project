package Miary.miniWeb.MemberManager.profile.profileImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long>, CustomProfileImageRepository {
}
