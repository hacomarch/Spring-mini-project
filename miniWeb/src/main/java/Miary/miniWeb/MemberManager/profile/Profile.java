package Miary.miniWeb.MemberManager.profile;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.profile.profileImage.ProfileImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="profile")
public class Profile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileIdx;

    private String nickname;
    private String aboutMe;

    @OneToMany(mappedBy = "profileImage", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ProfileImage> profileImage = new ArrayList<>();

    @OneToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "memberProfile")
    private Member memberProfile;

}
