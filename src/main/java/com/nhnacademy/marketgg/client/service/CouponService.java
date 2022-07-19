package com.nhnacademy.marketgg.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.marketgg.client.dto.request.CouponRequest;
import com.nhnacademy.marketgg.client.dto.response.CouponRetrieveResponse;

import java.util.List;

public interface CouponService {

    void createCoupon(CouponRequest couponRequest) throws JsonProcessingException;

    List<CouponRetrieveResponse> retrieveCoupons();

    void updateCoupon(CouponRequest couponRequest) throws JsonProcessingException;

}
