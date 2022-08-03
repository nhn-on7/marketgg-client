package com.nhnacademy.marketgg.client.interceptor;

import com.nhnacademy.marketgg.client.jwt.JwtInfo;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * RestTemplate 을 이용한 요청 시 자동으로 헤더에 JWT 를 추가하기 위한 클래스입니다.
 *
 * @version 1.0.0
 * @see <a href="https://www.baeldung.com/spring-rest-template-interceptor">RestTemplate Interceptor</a>
 * @author 윤동열
 */
@Slf4j
@RequiredArgsConstructor
public class JwtRefreshInterceptor implements ClientHttpRequestInterceptor {

    private final RedisTemplate<String, JwtInfo> redisTemplate;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        ServletRequestAttributes requestAttributes
            = Objects.requireNonNull(
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        Object session
            = requestAttributes.getRequest().getSession(true).getAttribute(JwtInfo.SESSION_ID);

        if (Objects.isNull(session)) {
            return execution.execute(httpRequest, body);
        }

        String sessionId = (String) session;

        JwtInfo jwtInfo = (JwtInfo) redisTemplate.opsForHash().get(sessionId, JwtInfo.JWT_REDIS_KEY);

        if (Objects.isNull(jwtInfo)) {
            return execution.execute(httpRequest, body);
        }

        log.info("RestTemplate Interceptor");
        httpRequest.getHeaders().setBearerAuth(jwtInfo.getJwt());

        return execution.execute(httpRequest, body);
    }

}
