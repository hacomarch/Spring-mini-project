package Miary.miniWeb.guestbook.reply;

import Miary.miniWeb.guestbook.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByReplyIdx(Long replyIdx);

    List<Reply> findByGuestBookCmtIdx(GuestBook guestBook);
}
