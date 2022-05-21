package Miary.miniWeb.MemberManager;

import Miary.miniWeb.MemberManager.profile.Profile;
import Miary.miniWeb.guestbook.GuestBook;
import Miary.miniWeb.diary.Diary;
import Miary.miniWeb.guestbook.reply.Reply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name="member")
public class Member {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long memberIdx;

    private String id;
    private String password;

    private String name;
    @Column
    private int age;

    private String tel;

    @Column
    @CreatedDate //생성된 시간 정보, @LastModifiedDate : 수정된 시간 정보
    private LocalDateTime joindate;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Diary> diary = new HashSet<>();

    @OneToMany(mappedBy = "guestBookMember", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<GuestBook> guestBooks = new HashSet<>();

    @OneToMany(mappedBy = "replyMember", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Reply> replySet = new HashSet<>();

    @OneToOne(mappedBy = "memberProfile", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Profile profile = new Profile();


}
