package com.nhnacademy.marketgg.client.repository.impl;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.nhnacademy.marketgg.client.dto.request.LoginRequest;
import com.nhnacademy.marketgg.client.exception.LoginFailException;
import com.nhnacademy.marketgg.client.exception.ServerException;
import com.nhnacademy.marketgg.client.jwt.JwtInfo;
import com.nhnacademy.marketgg.client.repository.AuthRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client Server 와 Auth Server 사이에서 데이터를 주고 받습니다.
 *
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthAdapter implements AuthRepository {

    @Value("${marketgg.gateway-origin}")
    private String authServerUrl;

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, JwtInfo> redisTemplate;

    /**
     * 로그인 시 서버에 로그인 정보를 전송합니다.
     *
     * @param loginRequest - 사용자가 입력한 이메일과 비밀번호
     */
    @Override
    public void doLogin(final LoginRequest loginRequest, final String sessionId) {
        log.info("login start");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(loginRequest, httpHeaders);

        ResponseEntity<Void> response =
            restTemplate.postForEntity(authServerUrl + "/auth/login", httpEntity, Void.class);

        this.checkStatus(response);

        HttpHeaders headers = response.getHeaders();

        String jwt = Objects.requireNonNull(headers.get(AUTHORIZATION)).get(0);
        String expire = Objects.requireNonNull(headers.get(JwtInfo.JWT_EXPIRE)).get(0);

        JwtInfo jwtInfo = new JwtInfo(jwt, expire);
        Instant instant = jwtInfo.getJwtExpireDate()
                                 .plus(6, ChronoUnit.DAYS)
                                 .plus(30, ChronoUnit.MINUTES)
                                 .atZone(ZoneId.systemDefault())
                                 .toInstant();
        Date expireDate = Date.from(instant);

        log.info("login success: {}", jwtInfo.getJwt());
        redisTemplate.opsForHash().put(sessionId, JwtInfo.JWT_KEY, jwtInfo);
        redisTemplate.expireAt(sessionId, expireDate);
    }

    private void checkStatus(ResponseEntity<?> response) {
        if (response.getStatusCode().is4xxClientError()) {
            log.info("Login Fail - http status: {}", response.getStatusCode());
            throw new LoginFailException();
        }

        if (response.getStatusCode().is5xxServerError()) {
            log.info("Login Fail - http status: {}", response.getStatusCode());
            throw new ServerException();
        }

        if (Objects.isNull(response.getHeaders().get(AUTHORIZATION))
            || Objects.isNull(response.getHeaders().get(JwtInfo.JWT_EXPIRE))) {
            log.info("Empty header");
            throw new LoginFailException();
        }
    }

}