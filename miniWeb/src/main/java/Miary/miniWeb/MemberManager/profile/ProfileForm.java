package Miary.miniWeb.MemberManager.profile;

import Miary.miniWeb.MemberManager.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class ProfileForm {

    private Long profileIdx;

    private String nickname;
    private String aboutMe;

    private List<MultipartFile> profileImage;

    private Member member;

    public ProfileForm(String nickname, String aboutMe) {
        this.nickname = nickname;
        this.aboutMe = aboutMe;
    }

    public ProfileForm(){}
}
