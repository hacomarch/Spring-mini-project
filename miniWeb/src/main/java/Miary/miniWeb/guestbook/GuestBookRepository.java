package Miary.miniWeb.guestbook;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    GuestBook findByCommentIdx(Long commentIdx);
}
