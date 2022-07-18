package com.nhnacademy.marketgg.client.service;

import com.nhnacademy.marketgg.client.dto.request.ProductInquiryRequest;
import com.nhnacademy.marketgg.client.dto.response.ProductInquiryResponse;

import java.util.List;

public interface ProductInquiryService {

    List<ProductInquiryResponse> retrieveProductInquiryByProductId(final Long memberId);

    List<ProductInquiryResponse> retrieveProductInquiryByMemberId(final Long memberId);

    void updateProductInquiryReply(final Long productId);

    void createProductInquiry(final Long productId, final ProductInquiryRequest productInquiryRequest);

    void deleteProductInquiry(final Long productId, final Long inquiryId);

}
