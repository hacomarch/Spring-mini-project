package Miary.miniWeb.Controller;

import Miary.miniWeb.MemberManager.Member;

import Miary.miniWeb.MemberManager.profile.Profile;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController{

    private final DiaryService diaryService;
    private final ImageService imageService;
    private final ProfileService profileService;
    private final ProfileImageService profileImageService;

    @GetMapping("/")
    public String homeLogin(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);

        Diary latestDiary = new Diary();
        List<Image> imageFiles = new ArrayList<>();
        List<Diary> diaryList = diaryService.findByMemberId(loginMember.getMemberIdx());

        Profile profile = new Profile();
        List<ProfileImage> profileImage = new ArrayList<>();
        List<Profile> profileList = profileService.findByMemberId(loginMember.getMemberIdx());

        if (!diaryList.isEmpty()) {
            latestDiary = diaryList.get(diaryList.size() - 1);
            imageFiles = imageService.findByDiaryIdx(latestDiary);

            //역순으로 정렬해서, 만든 날짜 기준 정렬해서 모델에 저장
            Collections.sort(diaryList, Collections.reverseOrder());
        }

        if (!profileList.isEmpty()) {
            profile = profileList.get(profileList.size() - 1);
            profileImage = profileImageService.findProfile(profile);
        }else{
            profile.setNickname("닉네임을 입력하세요.");
            profile.setAboutMe("자기소개를 입력하세요.");
            profileService.saveProfile(profile);
        }

        model.addAttribute("imageFiles", imageFiles);
        model.addAttribute("diaryList", diaryList);
        model.addAttribute("profile", profile);
        model.addAttribute("profileImage", profileImage);

        return "loginHome";
    }
}
