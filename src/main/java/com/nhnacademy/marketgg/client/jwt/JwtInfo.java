package com.nhnacademy.marketgg.client.jwt;

import com.nhnacademy.marketgg.client.exception.ServerException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.Getter;

@Getter
public class JwtInfo implements Serializable {

    public static final String JWT_REDIS_KEY = "JWT";
    public static final String JWT_EXPIRE = "JWT-Expire";
    public static final String SESSION_ID = "SESSION-ID";
    public static final String BEARER = "Bearer ";
    public static final int BEARER_LENGTH = 7;

    private final String jwt;
    private final LocalDateTime jwtExpireDate;

    public JwtInfo(String jwt, String jwtExpireDate) {
        this.jwt = jwt;
        try {
            this.jwtExpireDate = LocalDateTime.parse(jwtExpireDate);
        } catch (Exception e) {
            throw new ServerException();
        }
    }

    public Date localDateTimeToDateForRenewToken(LocalDateTime expiredTime) {
        Instant instant = expiredTime
            .plus(6, ChronoUnit.DAYS)
            .plus(30, ChronoUnit.MINUTES)
            .atZone(ZoneId.systemDefault())
            .toInstant();
        return Date.from(instant);
    }

}
