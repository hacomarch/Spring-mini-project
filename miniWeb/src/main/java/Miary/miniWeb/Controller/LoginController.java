package Miary.miniWeb.Controller;

import Miary.miniWeb.MemberManager.Member;
import Miary.miniWeb.diary.Diary;
import Miary.miniWeb.diary.DiaryForm;
import Miary.miniWeb.login.LoginForm;
import Miary.miniWeb.service.LoginService;
import Miary.miniWeb.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping(value = "/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login/loginForm";
    }

    @PostMapping(value = "/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        log.info("login =  {}", loginMember.getName());

        HttpSession session = request.getSession(); //세션 생성
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember); //세션에 로그인 회원 정보 보관

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //세션 제거
        }
        return "redirect:/login";
    }

    @GetMapping("/updateLoginHome")
    public String updateLoginHome(){
        return "redirect:/";
    }

    @RequestMapping(value = "/memberSessionCheck", method = RequestMethod.POST)
    @ResponseBody
    public String memberSessionCheck(HttpSession session) {
        String sessionUserName = (String) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return sessionUserName;
    }

}
