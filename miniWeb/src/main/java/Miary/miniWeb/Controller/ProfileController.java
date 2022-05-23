package Miary.miniWeb.Controller;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.profile.Profile;
import Miary.miniWeb.MemberManager.profile.ProfileForm;
import Miary.miniWeb.MemberManager.profile.profileImage.ProfileImage;
import Miary.miniWeb.service.ProfileImageService;
import Miary.miniWeb.service.ProfileService;
import Miary.miniWeb.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileImageService profileImageService;

//    @GetMapping("/profile/new")
//    public String profileForm(Model model) {
//        List<Profile> profiles = profileService.findAll();
//        ProfileForm form = new ProfileForm("닉네임을 입력하세요.", "자기소개를 입력하세요.");
//        model.addAttribute("profileForm", form);
//        model.addAttribute("profileList", profiles);
//        return "profile/write";
//    }
//
//    @PostMapping("/profile/new")
//    public String profileWrite(@Valid @ModelAttribute("profileForm") ProfileForm profileForm, HttpServletRequest request, Model model) throws IOException {
//
//        HttpSession session = request.getSession(false);
//
//        Profile profile = new Profile();
//        profile.setProfileIdx(profileForm.getProfileIdx());
//        profile.setNickname(profileForm.getNickname());
//
//        profile.setAboutMe(profileForm.getAboutMe());
//        profile.setMemberProfile((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));
//
//        List<ProfileImage> profileImages = profileImageService.storeFiles(profileForm.getProfileImage(), profile);
//        profile.setProfileImage(profileImages);
//
//        profileService.saveProfile(profile);
//
//
//        return "redirect:/";
//    }

    @GetMapping("/profile/{profileIdx}/edit")
    public String updateProfileForm(@PathVariable("profileIdx") Long profileIdx, Model model) {
        Profile profile = profileService.findByProfileId(profileIdx);
        List<ProfileImage> profileImages = profileImageService.findProfile(profile);

        ProfileForm form = new ProfileForm();
        form.setProfileIdx(profileIdx);
        form.setNickname(profile.getNickname());
        form.setAboutMe(profile.getAboutMe());
        form.setMember(profile.getMemberProfile());
        
        model.addAttribute("profileForm", form);
        model.addAttribute("profileImage", profileImages);
        return "profile/updateProfileForm";
    }

    @PostMapping("/profile/{profileIdx}/edit")
    public String updateProfile(@Valid @ModelAttribute("profileForm") ProfileForm form, @PathVariable("profileIdx") Long profileIdx, HttpServletRequest request, BindingResult result) throws IOException {
        HttpSession session = request.getSession(false);

        if (result.hasErrors()) {
            return "profile/updateProfileForm";
        }

        Profile profile = new Profile();
        profile.setProfileIdx(form.getProfileIdx());

        Profile byNickname = profileService.findByNickname(form.getNickname());
        Profile profile1 = profileService.findByProfileId(profileIdx);
        //닉네임 중복 확인
        if (byNickname != null && (byNickname.getNickname() != profile1.getNickname())) {
            result.reject("validateNickname", "닉네임이 중복됩니다.");
            return "profile/updateProfileForm";
        }

        profile.setNickname(form.getNickname());
        profile.setAboutMe(form.getAboutMe());
        profile.setMemberProfile((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));

        profileImageService.deleteProfileImage(profile);
        List<ProfileImage> images = profileImageService.storeFiles(form.getProfileImage(), profile);
        profile.setProfileImage(images);

        profileService.saveProfile(profile);

        return "redirect:/";
    }

    @GetMapping("/{profileIdx}/profileImageDelete")
    public String deleteProfileImage(@PathVariable("profileIdx") Long profileIdx) {
        Profile byProfileId = profileService.findByProfileId(profileIdx);
        profileImageService.deleteProfileImage(byProfileId);
        return "redirect:/";
    }

}
