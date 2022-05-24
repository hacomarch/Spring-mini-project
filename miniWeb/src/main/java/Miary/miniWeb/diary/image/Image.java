package Miary.miniWeb.diary.image;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.diary.Diary;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
@Table(name="image")
public class Image implements Comparable<Image>{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadFileName;
    private String storeFileName;

    @ManyToOne(targetEntity = Diary.class, fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "diaryIdx")
    private Diary diary;

    public void setDiary(Diary diary){
        this.diary = diary;
        diary.getImageFiles().add(this);
    }


    @Override
    public int compareTo(Image image) {
        return this.id.compareTo(image.id);
    }
}
