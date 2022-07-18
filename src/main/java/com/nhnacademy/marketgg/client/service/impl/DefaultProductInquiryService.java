package com.nhnacademy.marketgg.client.service.impl;

import com.nhnacademy.marketgg.client.dto.request.ProductInquiryRequest;
import com.nhnacademy.marketgg.client.dto.response.ProductInquiryResponse;
import com.nhnacademy.marketgg.client.repository.ProductInquiryRepository;
import com.nhnacademy.marketgg.client.service.ProductInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultProductInquiryService implements ProductInquiryService {

    private final ProductInquiryRepository productInquiryRepository;

    @Override
    public List<ProductInquiryResponse> retrieveProductInquiryByProductId(final Long memberId) {
        return productInquiryRepository.retrieveProductInquiryByProductId(memberId);
    }

    @Override
    public List<ProductInquiryResponse> retrieveProductInquiryByMemberId(final Long memberId) {
        return productInquiryRepository.retrieveProductInquiryByMemberId(memberId);
    }

    @Override
    public void updateProductInquiryReply(final Long productId) {
        productInquiryRepository.updateProductInquiryReply(productId);
    }

    @Override
    public void createProductInquiry(final Long productId,
                                     final ProductInquiryRequest productInquiryRequest) {
        productInquiryRepository.createProductInquiry(productInquiryRequest);
    }

    @Override
    public void deleteProductInquiry(final Long productId, final Long inquiryId) {
        productInquiryRepository.deleteProductInquiry(productId, inquiryId);
    }

}
