package Miary.miniWeb.diary;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.diary.image.Image;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="diary")
public class Diary implements Comparable<Diary>{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long diaryIdx;

    private String title;
    private String content;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "writerIdx")
    private Member member;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime updated;

    @OneToMany(mappedBy = "diary", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Image> imageFiles = new ArrayList<>();

    @Transactional
    public void setMember(Member member) {
        this.member = member;
        member.getDiary().add(this);
    }

    @Transactional
    public Member getMember() {
        return member;
    }

    @Override
    public int compareTo(Diary diary) {
        return this.created.compareTo(diary.created);
    }
}
