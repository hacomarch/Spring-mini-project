package Miary.miniWeb.MemberManager.profile.profileImage;

import Miary.miniWeb.MemberManager.profile.Profile;

import java.util.List;
import java.util.Optional;

public interface CustomProfileImageRepository {

    public List<ProfileImage> findProfile(Profile profile);
}
