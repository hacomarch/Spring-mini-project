package Miary.miniWeb.MemberManager.profile;

import java.util.List;

public interface CustomProfileRepository {

    public List<Profile> findByMemberId(Long memberIdx);

    public Profile findOne(Long profileIdx);

}
