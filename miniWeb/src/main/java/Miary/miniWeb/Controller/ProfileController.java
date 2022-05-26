package Miary.miniWeb.Controller;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.profile.Profile;
import Miary.miniWeb.MemberManager.profile.ProfileForm;
import Miary.miniWeb.MemberManager.profile.profileImage.ProfileImage;
import Miary.miniWeb.diary.Diary;
import Miary.miniWeb.diary.image.Image;
import Miary.miniWeb.service.DiaryService;
import Miary.miniWeb.service.ImageService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final DiaryService diaryService;
    private final ImageService imageService;
    private final ProfileService profileService;
    private final ProfileImageService profileImageService;

    @GetMapping("/profile/{profileIdx}/edit")
    public String updateProfileForm(@PathVariable("profileIdx") Long profileIdx, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("memberIdx", loginMember.getMemberIdx());

        Profile profile = profileService.findByProfileId(profileIdx);
        List<ProfileImage> profileImages = profileImageService.findProfile(profile);

        ProfileForm form = new ProfileForm();
        form.setProfileIdx(profileIdx);
        form.setNickname(profile.getNickname());
        form.setAboutMe(profile.getAboutMe());
        form.setMember(profile.getMemberProfile());
        
        model.addAttribute("profileForm", form);
        model.addAttribute("profileImage", profileImages);

        Diary latestDiary = new Diary();
        List<Image> imageFiles = new ArrayList<>();
        List<Diary> diaryList = diaryService.findByMemberId(loginMember.getMemberIdx());

        if (!diaryList.isEmpty()) {
            latestDiary = diaryList.get(diaryList.size() - 1);
            imageFiles = imageService.findByDiary(latestDiary);

            //역순으로 정렬해서, 만든 날짜 기준 정렬해서 모델에 저장
            Collections.sort(diaryList, Collections.reverseOrder());
        }

        model.addAttribute("imageFiles", imageFiles);
        model.addAttribute("diaryList", diaryList);
        return "profile/updateProfileForm";
    }

    @PostMapping("/profile/{profileIdx}/edit")
    public String updateProfile(@Valid @ModelAttribute("profileForm") ProfileForm form, @PathVariable("profileIdx") Long profileIdx, HttpServletRequest request, BindingResult result, Model model) throws IOException {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("memberIdx", loginMember.getMemberIdx());

        if (result.hasErrors()) {
            return "profile/updateProfileForm";
        }

        Profile profile = new Profile();
        profile.setProfileIdx(form.getProfileIdx());

        Profile byNickname = profileService.findByNickname(form.getNickname());

        //닉네임 중복 확인
        if (byNickname != null) {
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

        Diary latestDiary = new Diary();
        List<Image> imageFiles = new ArrayList<>();
        List<Diary> diaryList = diaryService.findByMemberId(loginMember.getMemberIdx());

        if (!diaryList.isEmpty()) {
            latestDiary = diaryList.get(diaryList.size() - 1);
            imageFiles = imageService.findByDiary(latestDiary);

            //역순으로 정렬해서, 만든 날짜 기준 정렬해서 모델에 저장
            Collections.sort(diaryList, Collections.reverseOrder());
        }

        model.addAttribute("imageFiles", imageFiles);
        model.addAttribute("diaryList", diaryList);
        model.addAttribute("profile", profile);
        model.addAttribute("profileImage", profile.getProfileImage());

        return "redirect:/";
    }

    @GetMapping("/{profileIdx}/profileImageDelete")
    public String deleteProfileImage(@PathVariable("profileIdx") Long profileIdx, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("memberIdx", loginMember.getMemberIdx());

        Profile byProfileId = profileService.findByProfileId(profileIdx);
        profileImageService.deleteProfileImage(byProfileId);
        return "redirect:/";
    }

}
