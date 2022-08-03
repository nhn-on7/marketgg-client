package com.nhnacademy.marketgg.client.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Optional;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Web Configuration 을 설정할 수 있습니다.
 *
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class WebConfig {

    /**
     * RestTemplate 을 원하는 값으로 설정 후 반환합니다.
     *
     * @param builder - RestTemplate 의 설정을 변경할 수 있는 Builder 객체입니다.
     * @return 원하는 값으로 설정한 RestTemplate 객체를 반환합니다.
     * @since 1.0.0
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setReadTimeout(Duration.ofSeconds(10L))
            .setConnectTimeout(Duration.ofSeconds(5L))
            .build();
    }

    /**
     * GATEWAY 의 IP 와 PORT 번호를 반환합니다.
     *
     * @param ip - GATEWAY 의 URI 입니다.
     * @return GATEWAY 의 IP 와 PORT 번호를 반환합니다.
     * @since 1.0.0
     */

    @Bean
    public String gatewayIp(@Value("${gg.gateway-origin}") String ip) {
        return ip;
    }

    /**
     * Html 의 Form 태그를 Get, Post 맵핑 이외릐 메소드로 ACTION 할 수 있게 해줍니다.
     *
     * @return HiddenHttpMethodFilter 객체를 반환합니다.
     * @since 1.0.0
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setMaxUploadSize(50000000);
        return commonsMultipartResolver;
    }

    @Bean
    CloseableHttpClient httpClient(final @Value("${gg.keystore.type}") String keystoreType,
                                   final @Value("${gg.keystore.path}") String keystorePath,
                                   final @Value("${gg.keystore.password}") String keystorePassword,
                                   final @Value("${gg.protocol}") String protocol) {

        SSLContext sslContext;

        try {

            KeyStore clientStore = KeyStore.getInstance(keystoreType);
            Resource resource = new ClassPathResource(keystorePath);
            clientStore.load(resource.getInputStream(), keystorePassword.toCharArray());

            sslContext = SSLContexts.custom()
                                    .setProtocol(protocol)
                                    .loadKeyMaterial(clientStore, keystorePassword.toCharArray())
                                    .loadTrustMaterial(new TrustSelfSignedStrategy())
                                    .build();

        } catch (GeneralSecurityException gse) {
            log.error("", gse);
            throw new IllegalArgumentException(gse);
        } catch (IOException ie) {
            log.error("", ie);
            throw new UncheckedIOException(ie);
        }

        SSLConnectionSocketFactory socketFactory
            = new SSLConnectionSocketFactory(Optional.ofNullable(sslContext).orElseThrow());

        return HttpClients.custom()
                          .setSSLSocketFactory(socketFactory)
                          .setMaxConnTotal(100)
                          .setMaxConnPerRoute(5)
                          .build();
    }

    @Bean
    HttpComponentsClientHttpRequestFactory requestFactory(final HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(5_000);
        requestFactory.setConnectTimeout(3_000);
        requestFactory.setHttpClient(httpClient);

        return requestFactory;
    }

    @Bean(name = "clientCertificateAuthenticationRestTemplate")
    public RestTemplate clientCertificationRestTemplate(final HttpComponentsClientHttpRequestFactory requestFactory) {
        return new RestTemplate(requestFactory);
    }

}
