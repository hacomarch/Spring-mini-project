package Miary.miniWeb.guestbook;

import Miary.miniWeb.MemberManager.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class GuestBookForm {

    private Long commentIdx;

    private String comments;

    private Member guestBookMember;

    private LocalDateTime created;
}
