package Miary.miniWeb.service;

import Miary.miniWeb.guestbook.GuestBook;
import Miary.miniWeb.guestbook.GuestBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestBookService {

    @Autowired
    GuestBookRepository guestBookRepository;

    //방명록 저장
    @Transactional
    public Long saveComment(GuestBook guestBook) {
        guestBookRepository.save(guestBook);
        return guestBook.getCommentIdx();
    }

    //전체 방명록 조회
    public List<GuestBook> findAllComments(){
        return guestBookRepository.findAll();
    }

    //글 id로 방명록 조회
    public GuestBook findByCommentIdx(Long commentIdx){
        return guestBookRepository.findByCommentIdx(commentIdx);
    }

    //방명록 삭제
    @Modifying
    @Transactional
    public Long deleteComment(GuestBook guestBook) {
        guestBookRepository.delete(guestBook);
        return guestBook.getCommentIdx();
    }

    //페이징
    public Page<GuestBook> pageGuestBookList(int page) {
        return guestBookRepository.findAll(PageRequest.of(page, 4, Sort.by(Sort.Direction.DESC, "commentIdx")));
    }
}
