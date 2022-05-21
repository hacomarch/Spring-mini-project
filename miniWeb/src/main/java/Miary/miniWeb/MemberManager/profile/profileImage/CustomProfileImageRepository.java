package Miary.miniWeb.MemberManager.profile.profileImage;

import Miary.miniWeb.MemberManager.profile.Profile;

import java.util.List;

public interface CustomProfileImageRepository {

    public List<ProfileImage> findProfile(Profile profile);
}
