package Miary.miniWeb.guestbook.reply;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.guestbook.GuestBook;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="reply")
public class Reply implements Comparable<Reply>{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyIdx;

    private String replyContent;

    @ManyToOne(targetEntity = GuestBook.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "commentIdx")
    private GuestBook guestBookCmtIdx;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "memberIdx")
    private Member replyMember;

    @Override
    public int compareTo(Reply reply) {
        return this.replyIdx.compareTo(reply.replyIdx);
    }
}
