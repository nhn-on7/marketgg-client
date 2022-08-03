package com.nhnacademy.marketgg.client.repository;

import com.nhnacademy.marketgg.client.dto.request.MemberSignupToShopMember;
import com.nhnacademy.marketgg.client.dto.request.MemberWithdrawRequest;
import com.nhnacademy.marketgg.client.dto.response.MemberUpdateToAuthResponse;
import java.time.LocalDateTime;

/**
 * 회원 Repository 입니다.
 *
 * @version 1.0.0
 */
public interface MemberRepository {

    /**
     * 지정한 회원의 GG PASS 갱신일시를 반환하는 메소드입니다.
     *
     * @param id - 갱신일시를 반환할 회원의 식별번호입니다.
     * @return 회원의 갱신일시를 반환합니다.
     * @since 1.0.0
     */
    LocalDateTime retrievePassUpdatedAt(final Long id);

    /**
     * 지정한 회원의 GG PASS 구독처리 메소드입니다.
     *
     * @param id - 구독 할 회원의 식별번호입니다.
     * @since 1.0.0
     */
    void subscribePass(final Long id);

    /**
     * 지정한 회원의 GG PASS 구독해지처리 메소드입니다.
     *
     * @param id - 구독해지 할 회원의 식별번호입니다.
     * @since 1.0.0
     */
    void withdrawPass(final Long id);

    /**
     * 회원가입 메소드입니다.
     *
     * @param signupRequestToShopMember - Shop 서버에 저장될 회원의 정보를 담은 객체입니다.
     */
    void signup(final MemberSignupToShopMember signupRequestToShopMember);

    void withdraw(final MemberWithdrawRequest deletedAt);

    void update(final MemberUpdateToAuthResponse memberUpdateToAuthResponse, final String sessionId);
}
