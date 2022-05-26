package Miary.miniWeb.service;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.MemberRepository;
import Miary.miniWeb.diary.Diary;
import Miary.miniWeb.diary.DiaryRepository;
import Miary.miniWeb.diary.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    @Autowired DiaryRepository diaryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired
    ImageRepository imageRepository;

    //글 저장
    @Transactional
    public Long saveDiary(Diary diary){
        diaryRepository.save(diary);
        return diary.getDiaryIdx();
    }

    //글 수정
    @Transactional
    public Long updateDiary(Diary diary){
        diaryRepository.saveAndFlush(diary);
        return diary.getDiaryIdx();
    }

    //전체 일기 조회
    public List<Diary> findAllDiary(){
        return diaryRepository.findAll();
    }

    public Diary findDiaryById(Long diary_idx){
        return diaryRepository.findById(diary_idx);
    }

    public List<Diary> findByMemberId(Long member_idx){
        return diaryRepository.findByMemberId(member_idx);
    }

    //제목으로 검색
    public List<Diary> findByTitle(String searchTitle) {
        return diaryRepository.findByTitleContaining(searchTitle);
    }

    //글 삭제
    @Modifying
    @Transactional
    public Long deleteDiary(Diary diary){
        imageRepository.deleteImage(diary);
        diaryRepository.deleteDiary(diary.getDiaryIdx());
        return diary.getDiaryIdx();
    }

    public Page<Diary> pageList(Long memberIdx, int page){
        Member member = memberRepository.findOne(memberIdx);
        return diaryRepository.findByMember(member, PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "diaryIdx")));
    }


}
