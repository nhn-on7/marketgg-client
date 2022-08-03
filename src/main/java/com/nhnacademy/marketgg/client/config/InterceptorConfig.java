package com.nhnacademy.marketgg.client.config;

import com.nhnacademy.marketgg.client.interceptor.JwtRefreshInterceptor;
import com.nhnacademy.marketgg.client.jwt.JwtInfo;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate Interceptor 를 RestTemplate 에 추가 설정하는 클래스입니다.
 *
 * @author 윤동열
 */
@Configuration
public class InterceptorConfig {

    /**
     * 객체가 생성되는 동시에 RestTemplate 에 RestTemplate Interceptor 를 추가합니다.
     * 스프링 빈 순환 참조 이슈가 있어 추후에 Interceptor 를 설정했습니다.
     *
     * @param restTemplate  - Interceptor 를 추가해주려는 RestTemplate
     * @param redisTemplate - Interceptor 가 JWT 를 참조 시 필요한 Redis
     */
    public InterceptorConfig(RestTemplate restTemplate,
                             RedisTemplate<String, JwtInfo> redisTemplate) {
        restTemplate.setInterceptors(List.of(new JwtRefreshInterceptor(redisTemplate)));
    }

}
