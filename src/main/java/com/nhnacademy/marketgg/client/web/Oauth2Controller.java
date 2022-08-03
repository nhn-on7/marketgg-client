package com.nhnacademy.marketgg.client.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.marketgg.client.jwt.JwtInfo;
import com.nhnacademy.marketgg.client.oauth2.GoogleProfile;
import com.nhnacademy.marketgg.client.service.OAuth2Service;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/google")
    public RedirectView googleLogin() {
        return new RedirectView(oAuth2Service.getRedirectUrl());
    }

    @GetMapping("/oauth2/code/google")
    public ModelAndView googleRedirect(@RequestParam String code, HttpSession httpSession,
                                       HttpSession session, HttpServletResponse response,
                                       RedirectAttributes redirectAttributes) throws JsonProcessingException {

        String sessionId = session.getId();

        Optional<GoogleProfile> googleProfile = oAuth2Service.getToken(code, sessionId);

        if (googleProfile.isPresent()) {
            redirectAttributes.addFlashAttribute("profile", googleProfile.get());
            response.addCookie(new Cookie(JwtInfo.SESSION_ID, sessionId));

            return new ModelAndView("redirect:/signup");  // 회원가입 폼
        }

        return new ModelAndView("redirect:/");
    }

}
