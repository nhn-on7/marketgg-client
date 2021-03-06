package com.nhnacademy.marketgg.client.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 상품 수정을 위한 DTO 입니다.
 *
 * @version 1.0.0
 */
@RequiredArgsConstructor
@Getter
public class ProductModifyRequest {

    private final String categoryCode;
    private final String name;
    private final String content;
    private final Long totalStock;
    private final Long price;
    private final String description;
    private final String unit;
    private final String deliveryType;
    private final String origin;
    private final String packageType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate expirationDate;
    private final String allergyInfo;
    private final String capacity;

}
