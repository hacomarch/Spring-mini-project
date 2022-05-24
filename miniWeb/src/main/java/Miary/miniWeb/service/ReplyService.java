package Miary.miniWeb.service;

import Miary.miniWeb.guestbook.GuestBook;
import Miary.miniWeb.guestbook.GuestBookRepository;
import Miary.miniWeb.guestbook.reply.Reply;
import Miary.miniWeb.guestbook.reply.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    GuestBookRepository guestBookRepository;

    //댓글 저장
    @Transactional
    public Long saveReply(Reply reply) {
        replyRepository.save(reply);
        return reply.getReplyIdx();
    }

    //댓글 삭제
    @Modifying
    @Transactional
    public Long deleteReply(Reply reply) {
        replyRepository.delete(reply);
        return reply.getReplyIdx();
    }

    public List<Reply> findReplyList(Long guestBookIdx){
        GuestBook byCommentIdx = guestBookRepository.findByCommentIdx(guestBookIdx);
        return replyRepository.findByGuestBookCmtIdx(byCommentIdx);
    }



}
