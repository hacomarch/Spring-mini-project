package Miary.miniWeb.MemberManager;

import Miary.miniWeb.MemberManager.profile.Profile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter
public class MemberForm {

    private Long memberIdx;

    @NotEmpty(message="회원 이름 필수")
    private String name;

    @NotEmpty(message="아이디 필수")
    private String id;

    @NotEmpty(message="비밀번호 필수")
    private String password;

    private String tel;
    private int age;

    private LocalDateTime joindate;

    private Profile profile;

}
