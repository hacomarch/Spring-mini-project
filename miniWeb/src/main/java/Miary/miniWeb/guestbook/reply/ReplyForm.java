package Miary.miniWeb.guestbook.reply;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.guestbook.GuestBook;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReplyForm {
    private Long replyIdx;

    private String replyContent;

    private GuestBook guestBook;

    private Member replyMember;

}
