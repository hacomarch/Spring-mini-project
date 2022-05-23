package Miary.miniWeb.Controller;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.diary.Diary;
import Miary.miniWeb.diary.DiaryForm;
import Miary.miniWeb.diary.image.Image;
import Miary.miniWeb.diary.message.Message;
import Miary.miniWeb.service.DiaryService;
import Miary.miniWeb.service.ImageService;
import Miary.miniWeb.session.SessionConst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;
    private final ImageService imageService;


    @GetMapping("/{memberIdx}/diary")
    public String list(@PathVariable("memberIdx") Long memberIdx, @RequestParam(required = false, defaultValue = "0", value = "page") int page, Model model) {
        List<Diary> diary = diaryService.findByMemberId(memberIdx);

        Page<Diary> listPage = diaryService.pageList(memberIdx, page);
        int totalPage = listPage.getTotalPages();

        model.addAttribute("page", listPage.getContent());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("memberIdx", memberIdx);
        return "diary/list";
    }

    @GetMapping("/diary/new")
    public String writeForm(Model model) {
        model.addAttribute("postForm", new DiaryForm());
        return "diary/write";
    }

    @PostMapping("/diary/new")
    public String write(@Valid @ModelAttribute("postForm") DiaryForm form, HttpServletRequest request, Model model) throws IOException {
        HttpSession session = request.getSession(false);

        Diary diary = new Diary();
        diary.setDiaryIdx(form.getDiaryIdx());
        diary.setTitle(form.getTitle());
        diary.setContent(form.getContent());
        diary.setMember((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));
        diary.setCreated(LocalDateTime.now());
        diary.setUpdated(form.getUpdated());

        List<Image> imageFiles = imageService.storeFiles(form.getImageFiles(), diary);
        diary.setImageFiles(imageFiles);

        diaryService.saveDiary(diary);

        model.addAttribute("diaryInfo", diary);
        model.addAttribute("imageFiles", imageFiles);

        return "diary/writeInfo";
    }


    @GetMapping("/diary/{diaryIdx}/Info")
    public String writeDisplay(@PathVariable("diaryIdx") Long diaryIdx, Model model){
        Diary diary = diaryService.findDiaryById(diaryIdx);
        model.addAttribute("diaryInfo", diary);

        List<Image> imageFiles = imageService.findByDiaryIdx(diary);

        model.addAttribute("imageFiles", imageFiles);
        return "diary/writeInfo";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + imageService.getFullPath(filename));
    }

    @GetMapping("/diary/{diaryIdx}/edit")
    public String updateDiaryForm(@PathVariable("diaryIdx") Long diaryIdx, Model model) {
        Diary diary = diaryService.findDiaryById(diaryIdx);
        List<Image> byDiaryIdx = imageService.findByDiaryIdx(diary);

        DiaryForm diaryEditForm = new DiaryForm();
        diaryEditForm.setDiaryIdx(diary.getDiaryIdx());
        diaryEditForm.setMember(diary.getMember());
        diaryEditForm.setUpdated(diary.getUpdated());
        diaryEditForm.setContent(diary.getContent());
        diaryEditForm.setCreated(diary.getCreated());
        diaryEditForm.setTitle(diary.getTitle());

        model.addAttribute("diaryInfo", diaryEditForm);
        model.addAttribute("imageFiles", byDiaryIdx);
        return "diary/updateDiaryForm";
    }

    @PostMapping("/diary/{diaryIdx}/edit")
    public String updateDiary(@ModelAttribute("diaryInfo") DiaryForm form, HttpServletRequest request, Model model) throws IOException {
        HttpSession session = request.getSession(false);

        Diary diary = new Diary();
        diary.setDiaryIdx(form.getDiaryIdx());
        diary.setUpdated(LocalDateTime.now());
        diary.setMember((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));
        diary.setContent(form.getContent());
        diary.setTitle(form.getTitle());
        diary.setCreated(form.getCreated());

        List<Image> imageFiles = new ArrayList<>();

        if(form.getImageFiles().isEmpty()) {//사진 수정 할 때
            imageService.deleteDiaryImage(diary);
            imageFiles = imageService.storeFiles(form.getImageFiles(), diary);
            diary.setImageFiles(imageFiles);
        }

        diaryService.updateDiary(diary);

        model.addAttribute("imageFiles", imageFiles);

        return "diary/writeInfo";
    }

    @GetMapping("/diary/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<Diary> diaryList = diaryService.findByTitle(keyword);

        model.addAttribute("diaryList", diaryList);
        return "diary/search";
    }

    @GetMapping("/diary/{diaryIdx}/delete")
    public String delete(@PathVariable("diaryIdx") Long diary_idx, Model model){
        model.addAttribute("data", new Message("삭제하시겠습니까?", "/"));

        Diary diary = diaryService.findDiaryById(diary_idx);
        diaryService.deleteDiary(diary);

        return "diary/deleteMessage";
    }


}
