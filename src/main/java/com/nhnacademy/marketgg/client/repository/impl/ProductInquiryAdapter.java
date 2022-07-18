package com.nhnacademy.marketgg.client.repository.impl;

import com.nhnacademy.marketgg.client.dto.request.ProductInquiryRequest;
import com.nhnacademy.marketgg.client.dto.response.ProductInquiryResponse;
import com.nhnacademy.marketgg.client.repository.ProductInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductInquiryAdapter implements ProductInquiryRepository {

    private static final String BASE_URL = "http://localhost:7080";
    private static final String SERVER_DEFAULT_PRODUCT_INQUIRY = "/shop/v1";

    private final RestTemplate restTemplate;

    @Override
    public void createProductInquiry(final ProductInquiryRequest productInquiryRequest) {

    }

    @Override
    public List<ProductInquiryResponse> retrieveProductInquiryByProductId(final Long memberId) {
        return null;
    }

    @Override
    public List<ProductInquiryResponse> retrieveProductInquiryByMemberId(final Long memberId) {
        return null;
    }

    @Override
    public void updateProductInquiryReply(final Long productId) {

    }

    @Override
    public void deleteProductInquiry(final Long productId, final Long inquiryId) {

    }

}
