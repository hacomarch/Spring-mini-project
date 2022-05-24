package Miary.miniWeb.guestbook;

import Miary.miniWeb.guestbook.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    GuestBook findByCommentIdx(Long commentIdx);

}
