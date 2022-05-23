package Miary.miniWeb.diary;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.diary.image.Image;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class DiaryForm {

    private Long diaryIdx;

    @NotEmpty(message="제목 필수")
    private String title;

    @NotEmpty(message="내용 필수")
    private String content;

    private Member member;

    private LocalDateTime created;
    private LocalDateTime updated;

    private List<MultipartFile> imageFiles;

}
