package Miary.miniWeb.Controller;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.MemberManager.profile.Profile;
import Miary.miniWeb.guestbook.GuestBook;
import Miary.miniWeb.guestbook.GuestBookForm;
import Miary.miniWeb.guestbook.reply.Reply;
import Miary.miniWeb.guestbook.reply.ReplyForm;
import Miary.miniWeb.service.GuestBookService;
import Miary.miniWeb.service.ProfileService;
import Miary.miniWeb.service.ReplyService;
import Miary.miniWeb.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GuestBookController {
    private final GuestBookService guestBookService;
    private final ReplyService replyService;
    private final ProfileService profileService;

    @GetMapping("/guestBook")
    public String commentList(@RequestParam(required = false, defaultValue = "0", value = "page") int page, Model model, HttpServletRequest request) {
        Page<GuestBook> listPage = guestBookService.pageGuestBookList(page);
        int totalPage = listPage.getTotalPages();

        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("memberIdx", loginMember.getMemberIdx());

        model.addAttribute("pageList", listPage.getContent());
        model.addAttribute("totalPage", totalPage);

        model.addAttribute("guestBookForm", new GuestBookForm());


        return "guestBook/list";
    }

    @PostMapping("/guestBook/new")
    public String commentWrite(@Valid @ModelAttribute("guestBookForm") GuestBookForm form, HttpServletRequest request, Model model) throws IOException{
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("memberIdx", loginMember.getMemberIdx());

        GuestBook guestBook = new GuestBook();
        guestBook.setCommentIdx(form.getCommentIdx());
        guestBook.setComments(form.getComments());
        guestBook.setCreated(LocalDateTime.now());
        guestBook.setGuestBookMember(loginMember);

        guestBookService.saveComment(guestBook);

        return "redirect:/guestBook";
    }

    @GetMapping("/{guestBookIdx}/reply/new")
    public String replyWriteForm(@PathVariable("guestBookIdx") Long guestBookIdx, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("memberIdx", loginMember.getMemberIdx());

        model.addAttribute("replyForm", new ReplyForm());

        List<Reply> replyList = replyService.findReplyList(guestBookIdx);
        Collections.sort(replyList, Collections.reverseOrder());
        model.addAttribute("replyInfo", replyList);

        GuestBook byCommentIdx = guestBookService.findByCommentIdx(guestBookIdx);
        model.addAttribute("content", byCommentIdx);

        return "reply/write";
    }

    @PostMapping("/{guestBookIdx}/reply/new")
    public String replyWrite(@PathVariable("guestBookIdx") Long guestBookIdx, @Valid @ModelAttribute("replyForm") ReplyForm replyForm, HttpServletRequest request, Model model) throws IOException {
        HttpSession session = request.getSession(false);
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("memberIdx", loginMember.getMemberIdx());

        Reply reply = new Reply();
        reply.setReplyIdx(replyForm.getReplyIdx());
        reply.setReplyContent(replyForm.getReplyContent());
        reply.setReplyMember(loginMember);

        GuestBook guestBook = guestBookService.findByCommentIdx(guestBookIdx);
        reply.setGuestBookCmtIdx(guestBook);

        replyService.saveReply(reply);

        List<Reply> replies = replyService.findReplyList(guestBookIdx);
        //최신순 댓글
        Collections.sort(replies, Collections.reverseOrder());
        model.addAttribute("replyInfo", replies);

        GuestBook byCommentIdx = guestBookService.findByCommentIdx(guestBookIdx);
        model.addAttribute("content", byCommentIdx);
        return "reply/write";

    }


}
