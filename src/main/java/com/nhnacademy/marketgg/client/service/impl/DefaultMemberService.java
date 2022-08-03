package com.nhnacademy.marketgg.client.service.impl;

import com.nhnacademy.marketgg.client.dto.request.EmailRequest;
import com.nhnacademy.marketgg.client.dto.request.MemberSignupRequest;
import com.nhnacademy.marketgg.client.dto.request.MemberUpdateToAuth;
import com.nhnacademy.marketgg.client.dto.request.MemberWithdrawRequest;
import com.nhnacademy.marketgg.client.dto.response.EmailExistResponse;
import com.nhnacademy.marketgg.client.dto.response.EmailUseResponse;
import com.nhnacademy.marketgg.client.dto.response.MemberSignupResponse;
import com.nhnacademy.marketgg.client.dto.response.MemberUpdateToAuthResponse;
import com.nhnacademy.marketgg.client.repository.AuthRepository;
import com.nhnacademy.marketgg.client.repository.MemberRepository;
import com.nhnacademy.marketgg.client.service.MemberService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LocalDateTime retrievePassUpdatedAt(final Long id) {
        return memberRepository.retrievePassUpdatedAt(id);
    }

    @Override
    public void subscribePass(final Long id) {
        memberRepository.subscribePass(id);
    }

    @Override
    public void withdrawPass(final Long id) {
        memberRepository.withdrawPass(id);
    }

    @Override
    public void update(final MemberUpdateToAuth memberUpdateToAuth, final String sessionId) {
        MemberUpdateToAuthResponse updateData = authRepository.update(memberUpdateToAuth, sessionId);
        memberRepository.update(updateData, sessionId);
    }

    @Override
    public void withdraw(final String sessionId) {
        MemberWithdrawRequest withdrawRequest = new MemberWithdrawRequest(LocalDateTime.now());
        authRepository.withdraw(withdrawRequest, sessionId);
        memberRepository.withdraw(withdrawRequest);
    }

    @Override
    public void doSignup(final MemberSignupRequest memberSignupRequest) {
        memberSignupRequest.encodePassword(passwordEncoder);

        MemberSignupResponse response =
            authRepository.signup(memberSignupRequest.getSignupRequestToAuth());

        memberRepository.signup(memberSignupRequest
            .getSignupRequestToShopMember(response.getUuid(), response.getReferrerUuid()));
    }

    @Override
    public EmailExistResponse checkEmail(final EmailRequest emailRequest) {
        return authRepository.checkEmail(emailRequest);
    }

    @Override
    public EmailUseResponse useEmail(final EmailRequest emailRequest) {

        EmailUseResponse emailUseResponse = authRepository.useEmail(emailRequest);
        Map<String, EmailUseResponse> useEmailResultMap = new HashMap<>();
        useEmailResultMap.put("isUseEmail", emailUseResponse);
        return useEmailResultMap.get("isUeEmail");

    }

}
