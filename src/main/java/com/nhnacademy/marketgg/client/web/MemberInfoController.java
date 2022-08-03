package com.nhnacademy.marketgg.client.web;

import com.nhnacademy.marketgg.client.annotation.NoAuth;
import com.nhnacademy.marketgg.client.dto.request.MemberSignupRequest;
import com.nhnacademy.marketgg.client.dto.request.MemberUpdateToAuth;
import com.nhnacademy.marketgg.client.exception.auth.UnAuthenticException;
import com.nhnacademy.marketgg.client.jwt.JwtInfo;
import com.nhnacademy.marketgg.client.service.MemberService;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 회원관리에 관련된 Controller 입니다.
 *
 * @version 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class MemberInfoController {

    private static final String INDEX = "index";

    private final MemberService memberService;

    /**
     * 회원가입 View.
     *
     * @return 회원가입 폼
     * @author 김훈민
     * @since 1.0.0
     */
    @NoAuth
    @GetMapping("/signup")
    public ModelAndView signup(@ModelAttribute(name = "member") MemberSignupRequest member) {
        return new ModelAndView("members/signup");
    }

    /**
     * 회원가입 요청을 받아 회원가입을 진행합니다.
     *
     * @param member - 회원가입에 필요한 요청 정보 객체입니다.
     * @return 회원가입 실행 후, 다시 Index 페이지로 이동합니다.
     * @author 김훈민
     * @since 1.0.0
     */
    @NoAuth
    @PostMapping("/signup")
    public ModelAndView doSignup(final @Valid @ModelAttribute(name = "member") MemberSignupRequest member,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("members/signup");
        }

        memberService.doSignup(member);
        return new ModelAndView("redirect:/");
    }

    /**
     * 회원정보 수정 요청을 받아 회원정보 수정 프로세스를 진행합니다.
     *
     * @param memberUpdateToAuth - 회원정보 수정에 필요한 요청 정보 객체 (Auth 정보만 수정됨)  입니다.
     * @param session            - session 이 없는 회원의 접근을 막기위한 파라미터 입니다.
     * @return 회원수정 실행 후, 다시 Index 페이지로 이동합니다.
     * @author 김훈민
     * @since 1.0.0
     */
    @PostMapping("/update")
    public ModelAndView doUpdate(@ModelAttribute MemberUpdateToAuth memberUpdateToAuth, HttpSession session)
        throws UnAuthenticException {

        String sessionId = (String) session.getAttribute(JwtInfo.SESSION_ID);

        if (Objects.isNull(sessionId)) {
            throw new UnAuthenticException();
        }
        memberService.update(memberUpdateToAuth, sessionId);
        return new ModelAndView(INDEX);
    }

    /**
     * 회원탈퇴 요청을 받아 회원탈퇴 프로세스를 진행합니다.
     *
     * @param session - session 이 없는 회원의 접근을 막기위한 파라미터 입니다.
     * @return 회원탈퇴 실행 후, 다시 Index 페이지로 이동합니다.
     * @author 김훈민
     */
    @GetMapping("/withdraw")
    public ModelAndView doWithdraw(HttpSession session) throws UnAuthenticException {
        String sessionId = (String) session.getAttribute(JwtInfo.SESSION_ID);

        if (Objects.isNull(sessionId)) {
            throw new UnAuthenticException();
        }

        memberService.withdraw(sessionId);
        return new ModelAndView(INDEX);
    }

}
