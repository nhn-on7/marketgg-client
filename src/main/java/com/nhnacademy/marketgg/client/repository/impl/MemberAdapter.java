package com.nhnacademy.marketgg.client.repository.impl;

import com.nhnacademy.marketgg.client.dto.request.MemberSignupToShopMember;
import com.nhnacademy.marketgg.client.dto.request.MemberWithdrawRequest;
import com.nhnacademy.marketgg.client.dto.response.MemberUpdateToAuthResponse;
import com.nhnacademy.marketgg.client.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAdapter implements MemberRepository {

    private final String gateWayIp;
    private final RestTemplate restTemplate;

    private static final String DEFAULT_MEMBER = "/shop/v1/members/";

    @Override
    public LocalDateTime retrievePassUpdatedAt(final Long id) {
        HttpEntity<String> requestEntity = new HttpEntity<>(this.buildHeaders());
        ResponseEntity<LocalDateTime> response =
            restTemplate.exchange(gateWayIp + DEFAULT_MEMBER + id + "/ggpass", HttpMethod.GET, requestEntity,
                LocalDateTime.class);

        this.checkResponseUri(response);

        return response.getBody();
    }

    @Override
    public void subscribePass(final Long id) {
        HttpEntity<String> requestEntity = new HttpEntity<>(this.buildHeaders());
        ResponseEntity<Void> response =
            restTemplate.exchange(gateWayIp + DEFAULT_MEMBER + id + "/ggpass/subscribe", HttpMethod.POST, requestEntity,
                Void.class);

        this.checkResponseUri(response);
    }

    @Override
    public void withdrawPass(final Long id) {
        HttpEntity<String> requestEntity = new HttpEntity<>(this.buildHeaders());
        ResponseEntity<Void> response =
            restTemplate.exchange(gateWayIp + DEFAULT_MEMBER + id + "/ggpass/withdraw", HttpMethod.POST, requestEntity,
                Void.class);

        this.checkResponseUri(response);
    }

    @Override
    public void signup(final MemberSignupToShopMember signupRequestToShopMember) {
        HttpEntity<MemberSignupToShopMember> response = new HttpEntity<>(signupRequestToShopMember, buildHeaders());
        ResponseEntity<Void> exchange =
            restTemplate.exchange(gateWayIp + DEFAULT_MEMBER, HttpMethod.POST, response, Void.class);

        this.checkResponseUri(exchange);
    }

    @Override
    public void withdraw(final MemberWithdrawRequest deletedAt) {
        HttpEntity<MemberWithdrawRequest> response = new HttpEntity<>(deletedAt, buildHeaders());
        ResponseEntity<Void> exchange =
            restTemplate.exchange(gateWayIp + DEFAULT_MEMBER, HttpMethod.DELETE, response, Void.class);

        this.checkResponseUri(exchange);
    }

    @Override
    public void update(final MemberUpdateToAuthResponse memberUpdateToAuthResponse, String sessionId) {
        HttpEntity<MemberUpdateToAuthResponse> response = new HttpEntity<>(memberUpdateToAuthResponse, buildHeaders());
        ResponseEntity<Void> exchange =
            restTemplate.exchange(gateWayIp + DEFAULT_MEMBER, HttpMethod.PUT, response, Void.class);

        this.checkResponseUri(exchange);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        return httpHeaders;
    }

    private <T> void checkResponseUri(final ResponseEntity<T> response) {
        log.info(String.valueOf(response.getHeaders().getLocation()));
    }

}
