package com.nhnacademy.marketgg.client.service;

import com.nhnacademy.marketgg.client.dto.request.EmailRequest;
import com.nhnacademy.marketgg.client.dto.request.MemberSignupRequest;
import com.nhnacademy.marketgg.client.dto.request.MemberUpdateToAuth;
import com.nhnacademy.marketgg.client.dto.response.EmailExistResponse;
import com.nhnacademy.marketgg.client.dto.response.EmailUseResponse;
import java.time.LocalDateTime;

/**
 * 회원 서비스 입니다.
 *
 * @version 1.0.0
 */
public interface MemberService {

    void doSignup(MemberSignupRequest memberSignupRequest);

    EmailExistResponse checkEmail(EmailRequest emailRequest);

    EmailUseResponse useEmail(EmailRequest emailRequest);

    /**
     * 지정한 회원의 GG Pass 갱신일시를 반환하는 메소드입니다.
     *
     * @param id - 갱신일시를 반환할 회원의 식별번호입니다.
     * @return 회원의 갱신일시를 반환합니다.
     * @since 1.0.0
     */
    LocalDateTime retrievePassUpdatedAt(final Long id);

    /**
     * 지정한 회원의 GG Pass 구독처리를 위한 메소드입니다.
     *
     * @param id - GG Pass 구독을 진행 할 회원의 식별번호입니다.
     * @since 1.0.0
     */
    void subscribePass(final Long id);

    /**
     * 지정한 회원의 GG Pass 구독해지처리를 위한 메소드입니다.
     *
     * @param id - GG Pass 구독해지를 진행 할 회원의 식별번호입니다.
     * @since 1.0.0
     */
    void withdrawPass(final Long id);

    /**
     * 지정한 회원의 정보 수정을 위한 메소드입니다.
     *
     * @param memberUpdateToAuth - 회원이 입력한 수정정보입니다.
     */
    void update(final MemberUpdateToAuth memberUpdateToAuth, final String sessionId);

    void withdraw(final String sessionId);
}
