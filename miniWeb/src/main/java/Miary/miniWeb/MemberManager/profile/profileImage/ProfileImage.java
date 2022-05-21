package Miary.miniWeb.MemberManager.profile.profileImage;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.profile.Profile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="profileImage")
public class ProfileImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadFileName;
    private String storeFileName;

    @ManyToOne(targetEntity = Profile.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "profileIdx")
    private Profile profileImage;

    public void setProfileImage(Profile profile) {
        this.profileImage = profile;
        profileImage.getProfileImage().add(this);
    }


}
