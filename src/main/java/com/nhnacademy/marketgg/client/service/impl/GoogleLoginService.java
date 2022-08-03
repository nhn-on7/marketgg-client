package com.nhnacademy.marketgg.client.service.impl;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.client.dto.response.common.SingleResponse;
import com.nhnacademy.marketgg.client.jwt.JwtInfo;
import com.nhnacademy.marketgg.client.oauth2.GoogleProfile;
import com.nhnacademy.marketgg.client.oauth2.TokenRequest;
import com.nhnacademy.marketgg.client.service.OAuth2Service;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleLoginService implements OAuth2Service {

    @Value("${gg.google.client-id}")
    private String clientId;

    @Value("${gg.google.client-key}")
    private String clientSecret;

    @Value("${gg.google.redirect-uri}")
    private String redirectUri;

    @Value("${gg.google.login-request-url}")
    private String loginRequestUrl;

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, JwtInfo> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String getRedirectUrl() {
        String scope = "https://www.googleapis.com/auth/userinfo.profile "
            + "https://www.googleapis.com/auth/userinfo.email ";

        return "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId
            + "&redirect_uri=" + redirectUri + "&scope=" + scope + "&response_type=code";
    }

    @Override
    public Optional<GoogleProfile> getToken(final String code, final String sessionId) throws JsonProcessingException {
        TokenRequest parameters = new TokenRequest(clientId, clientSecret, code);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TokenRequest> httpEntity =
            new HttpEntity<>(parameters, headers);

        ResponseEntity<String> profileResponse =
            restTemplate.exchange(loginRequestUrl, HttpMethod.POST, httpEntity, String.class);

        return setLogin(profileResponse, sessionId);
    }

    private Optional<GoogleProfile> setLogin(final ResponseEntity<String> profileResponse, final String sessionId)
        throws JsonProcessingException {

        HttpHeaders headers = profileResponse.getHeaders();

        if (Objects.isNull(profileResponse.getHeaders().get(HttpHeaders.AUTHORIZATION)) || isInvalidHeader(headers)) {

            SingleResponse<GoogleProfile> responseResult =
                objectMapper.readValue(profileResponse.getBody(), new TypeReference<>() {
                });

            return Optional.ofNullable(responseResult.getData());
        }

        String jwt = Objects.requireNonNull(headers.get(AUTHORIZATION)).get(0);
        if (jwt.startsWith(JwtInfo.BEARER)) {
            jwt = jwt.substring(JwtInfo.BEARER_LENGTH);
        }
        String expire = Objects.requireNonNull(headers.get(JwtInfo.JWT_EXPIRE)).get(0);

        JwtInfo jwtInfo = new JwtInfo(jwt, expire);
        Date expireDate = jwtInfo.localDateTimeToDateForRenewToken(jwtInfo.getJwtExpireDate());

        log.info("login success: {}", jwtInfo.getJwt());
        redisTemplate.opsForHash().put(sessionId, JwtInfo.JWT_REDIS_KEY, jwtInfo);
        redisTemplate.expireAt(sessionId, expireDate);

        return Optional.empty();
    }

    private boolean isInvalidHeader(HttpHeaders headers) {
        return Objects.isNull(headers.get(AUTHORIZATION))
            || Objects.isNull(Objects.requireNonNull(headers.get(AUTHORIZATION)).get(0))
            || Objects.isNull(headers.get(JwtInfo.JWT_EXPIRE))
            || Objects.isNull(Objects.requireNonNull(headers.get(JwtInfo.JWT_EXPIRE)).get(0));
    }

}
