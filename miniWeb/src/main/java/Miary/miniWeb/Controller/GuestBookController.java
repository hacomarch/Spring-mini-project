package Miary.miniWeb.Controller;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.guestbook.GuestBook;
import Miary.miniWeb.guestbook.GuestBookForm;
import Miary.miniWeb.guestbook.reply.Reply;
import Miary.miniWeb.guestbook.reply.ReplyForm;
import Miary.miniWeb.service.GuestBookService;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GuestBookController {
    private final GuestBookService guestBookService;
    private final ReplyService replyService;

    @GetMapping("/guestBook")
    public String commentList(@RequestParam(required = false, defaultValue = "0", value = "page") int page, Model model) {
        Page<GuestBook> listPage = guestBookService.pageGuestBookList(page);
        int totalPage = listPage.getTotalPages();

        model.addAttribute("pageList", listPage.getContent());
        model.addAttribute("totalPage", totalPage);

        model.addAttribute("guestBookForm", new GuestBookForm());
        model.addAttribute("replyForm", new ReplyForm());

        return "guestBook/list";
    }

    @PostMapping("/guestBook/new")
    public String commentWrite(@Valid @ModelAttribute("guestBookForm") GuestBookForm form, HttpServletRequest request, Model model) throws IOException{
        HttpSession session = request.getSession(false);

        GuestBook guestBook = new GuestBook();
        guestBook.setCommentIdx(form.getCommentIdx());
        guestBook.setComments(form.getComments());
        guestBook.setCreated(LocalDateTime.now());
        guestBook.setGuestBookMember((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));

        guestBookService.saveComment(guestBook);

        return "redirect:/guestBook";
    }

    @PostMapping("/{guestBookIdx}/reply/new")
    public String replyWrite(@PathVariable("guestBookIdx") Long guestBookIdx, @Valid @ModelAttribute("replyForm") ReplyForm replyForm, HttpServletRequest request, Model model) throws IOException {
        HttpSession session = request.getSession(false);

        Reply reply = new Reply();
        reply.setReplyIdx(replyForm.getReplyIdx());
        reply.setReplyContent(replyForm.getReplyContent());
        reply.setReplyMember((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));

        GuestBook guestBook = guestBookService.findByCommentIdx(guestBookIdx);
        reply.setGuestBookCmtIdx(guestBook);

        replyService.saveReply(reply);
        model.addAttribute("replyInfo", reply);

        return "reply/info";

    }


}
