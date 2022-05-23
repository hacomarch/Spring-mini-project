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

    @GetMapping("/profile/new")
    public String profileForm(Model model) {
        List<Profile> profiles = profileService.findAll();
        model.addAttribute("profileForm", new ProfileForm());
        model.addAttribute("profileList", profiles);
        return "profile/write";
    }

    @PostMapping("/profile/new")
    public String profileWrite(@Valid @ModelAttribute("profileForm") ProfileForm profileForm, HttpServletRequest request, Model model, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "profile/write";
        }
        HttpSession session = request.getSession(false);

        List<Profile> byNickname = profileService.findByNickname(profileForm.getNickname());
        //닉네임 중복 확인
       if (!byNickname.isEmpty()) {
            result.reject("validateNickname", "닉네임이 중복됩니다.");
            return "profile/write";
        }

        Profile profile = new Profile();
        profile.setProfileIdx(profileForm.getProfileIdx());
        profile.setNickname(profileForm.getNickname());

        profile.setAboutMe(profileForm.getAboutMe());
        profile.setMemberProfile((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));

        List<ProfileImage> profileImages = profileImageService.storeFiles(profileForm.getProfileImage(), profile);
        profile.setProfileImage(profileImages);

        profileService.saveProfile(profile);


        return "redirect:/";
    }

    @GetMapping("/profile/{profileIdx}/edit")
    public String updateProfileForm(@PathVariable("profileIdx") Long profileIdx, Model model) {
        Profile profile = profileService.findByProfileId(profileIdx);

        ProfileForm form = new ProfileForm();
        form.setProfileIdx(profile.getProfileIdx());
        form.setNickname(profile.getNickname());
        form.setAboutMe(profile.getAboutMe());
        form.setMember(profile.getMemberProfile());

        profileImageService.deleteProfileImage(profile);

        model.addAttribute("profileUpdateForm", form);
        return "profile/updateProfileForm";
    }

    @PostMapping("/profile/{profileIdx}/edit")
    public String updateProfile(@Valid @ModelAttribute("profileUpdateForm") ProfileForm form, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);

        Profile profile = new Profile();
        profile.setProfileIdx(form.getProfileIdx());
        profile.setNickname(form.getNickname());
        profile.setAboutMe(form.getAboutMe());
        profile.setMemberProfile((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));

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
