package com.nhnacademy.marketgg.client.repository;

import com.nhnacademy.marketgg.client.dto.request.EmailRequest;
import com.nhnacademy.marketgg.client.dto.request.LoginRequest;
import com.nhnacademy.marketgg.client.dto.request.MemberSignupToAuth;
import com.nhnacademy.marketgg.client.dto.request.MemberUpdateToAuth;
import com.nhnacademy.marketgg.client.dto.request.MemberWithdrawRequest;
import com.nhnacademy.marketgg.client.dto.response.EmailExistResponse;
import com.nhnacademy.marketgg.client.dto.response.EmailUseResponse;
import com.nhnacademy.marketgg.client.dto.response.MemberSignupResponse;
import com.nhnacademy.marketgg.client.dto.response.MemberUpdateToAuthResponse;

/**
 * Client Server 와 Auth Server 사이에서 데이터를 주고 받습니다.
 *
 * @version 1.0.0
 */
public interface AuthRepository {

    /**
     * 사용자가 로그인 요청 시 인증서버에 로그인 요청을 전송합니다.
     *
     * @param loginRequest 사용자의 이메일, 비밀번호
     * @param sessionId JWT 를 Redis 에 넣기 위해 필요한 사용자의 Session ID
     */
    void doLogin(LoginRequest loginRequest, String sessionId);

    MemberSignupResponse signup(final MemberSignupToAuth signupRequestToAuth);

    EmailExistResponse checkEmail(final EmailRequest emailRequest);

    EmailUseResponse useEmail(final EmailRequest emailRequest);

    void withdraw(final MemberWithdrawRequest memberWithdrawRequest, final String sessionId);

    MemberUpdateToAuthResponse update(final MemberUpdateToAuth memberUpdateToAuth, final String sessionId);
    /**
     * 사용자가 로그아웃 요청 시 인증서버에 로그아웃 요청을 전송합니다.
     *
     * @param sessionId Redis 에 저장된 JWT 에 접근하기 위해 사용됩니다.
     */
    void logout(String sessionId);

}
