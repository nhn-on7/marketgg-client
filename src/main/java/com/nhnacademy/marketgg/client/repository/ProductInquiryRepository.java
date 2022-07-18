package com.nhnacademy.marketgg.client.repository;

import com.nhnacademy.marketgg.client.dto.request.ProductInquiryRequest;
import com.nhnacademy.marketgg.client.dto.response.ProductInquiryResponse;

import java.util.List;

public interface ProductInquiryRepository {
    List<ProductInquiryResponse> retrieveProductInquiryByProductId(final Long memberId);

    List<ProductInquiryResponse> retrieveProductInquiryByMemberId(final Long memberId);

    void updateProductInquiryReply(final Long productId);

    void createProductInquiry(final ProductInquiryRequest productInquiryRequest);

    void deleteProductInquiry(final Long productId, final Long inquiry);
}
