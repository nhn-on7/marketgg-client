package com.nhnacademy.marketgg.client.repository;

import com.nhnacademy.marketgg.client.dto.request.EmailRequest;
import com.nhnacademy.marketgg.client.dto.request.LoginRequest;
import com.nhnacademy.marketgg.client.dto.request.MemberSignupToAuth;
import com.nhnacademy.marketgg.client.dto.request.MemberUpdateToAuth;
import com.nhnacademy.marketgg.client.dto.response.EmailExistResponse;
import com.nhnacademy.marketgg.client.dto.response.EmailUseResponse;
import com.nhnacademy.marketgg.client.dto.response.MemberSignupResponse;
import com.nhnacademy.marketgg.client.dto.response.MemberUpdateToAuthResponse;

import java.time.LocalDateTime;

public interface AuthRepository {

    void doLogin(LoginRequest loginRequest, String sessionId);

    MemberSignupResponse signup(final MemberSignupToAuth signupRequestToAuth);

    EmailExistResponse checkEmail(final EmailRequest emailRequest);

    EmailUseResponse useEmail(final EmailRequest emailRequest);

    void withdraw(final LocalDateTime deletedAt, final String sessionId);

    MemberUpdateToAuthResponse update(final MemberUpdateToAuth memberUpdateToAuth, final String sessionId);
}
