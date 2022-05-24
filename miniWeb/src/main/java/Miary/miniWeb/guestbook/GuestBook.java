package Miary.miniWeb.guestbook;


import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.guestbook.reply.Reply;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="guestbook")
public class GuestBook {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "memberIdx")
    private Member guestBookMember;

    private String comments;

    @CreatedDate
    private LocalDateTime created;

    @OneToMany(mappedBy = "guestBookCmtIdx", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();
}