package com.nhnacademy.marketgg.client.web;


import com.nhnacademy.marketgg.client.domain.dto.response.ProductResponse;
import com.nhnacademy.marketgg.client.dto.request.ProductInquiryRequest;
import com.nhnacademy.marketgg.client.dto.response.ProductInquiryResponse;
import com.nhnacademy.marketgg.client.service.ProductInquiryService;
import com.nhnacademy.marketgg.client.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/shop/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductInquiryService productInquiryService;
    private static final String DEFAULT_PRODUCT_URI = "/products";

    /**
     * 기본 상품 index 페이지 GetMapping을 지원합니다.
     *
     * @return - 기본 뷰 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("products/index");
    }

    /**
     * 모든 상품 조회를 위한 GetMapping 을 지원 합니다.
     * 타임리프에서 products로 조회할 수 있습니다.
     * List 타입 입니다.
     *
     * @return - 모든 상품 조회 페이지를 리턴 합니다.
     * @since 1.0.0
     */
    @GetMapping
    public ModelAndView retrieveProducts() {

        List<ProductResponse> products = productService.retrieveProducts();

        ModelAndView mav = new ModelAndView(DEFAULT_PRODUCT_URI);
        mav.addObject("products", products);

        return mav;
    }

    /**
     * 상품 상세 조회를 위한 GetMapping 을 지원 합니다.
     * 타임리프에서 productDetails로 조회할 수 있습니다.
     *
     * @param productId - 상품의 PK 입니다.
     * @return - 상품 상세 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @GetMapping("/{productId}")
    public ModelAndView retrieveProductDetails(@PathVariable final Long productId) {

        ProductResponse productDetails = productService.retrieveProductDetails(productId);

        ModelAndView mav = new ModelAndView(DEFAULT_PRODUCT_URI + "/" + productId);
        mav.addObject("productDetails", productDetails);

        return mav;
    }

    /**
     * 카테고리로 상품을 조회하기 위한 GetMapping을 지원 합니다.
     * 타임리프에서 products로 조회할 수 있습니다.
     *
     * @param categorizationCode - 카테고리 대분류 입니다. ex) 100 - 상품
     * @param categoryCode       - 카테고리 소분류 입니다. ex) 101 - 채소
     * @return - 해당 카테고리를 가진 상품 목록 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @GetMapping("/{categorizationCode}/{categoryCode}")
    public ModelAndView retrieveProductsByCategory(@PathVariable final String categorizationCode,
                                                   @PathVariable final String categoryCode) {

        List<ProductResponse> products =
                productService.retrieveProductsByCategory(categorizationCode, categoryCode);

        ModelAndView mav = new ModelAndView("products/retrieve-products");
        mav.addObject("products", products);

        return mav;
    }
    // REVIEW 상품 문의 2: 상품 문의 등록
    /**
     * 상품 문의 생성을 하기 위한 GetMapping 을 지원합니다.
     *
     * @param productId - 상품 문의 생성을 위한 상품 id 입니다.
     * @return  - 상품 문의 작성 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @GetMapping("/{productId}/inquiries/create")
    public ModelAndView createProductInquiry(@PathVariable final Long productId) {
        ModelAndView mav = new ModelAndView("products/inquiry-form");
        mav.addObject("productId", productId);

        return mav;
    }

    // REVIEW 상품 문의 2: 상품 문의 등록
    /**
     * 상품 문의 생성을 하기 위한 PostMapping 을 지원합니다.
     *
     * @param productId - 상품 문의 생성을 하기 위한 상품 id 입니다.
     * @param productInquiryRequest - 상품 문의 내용을 담은 Request Dto 입니다.
     * @return 상품 문의 조회 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @PostMapping("/{productId}/inquiries/create")
    public ModelAndView createProductInquiry(@PathVariable final Long productId,
                                             @ModelAttribute final ProductInquiryRequest productInquiryRequest) {

        productInquiryService.createProductInquiry(productId, productInquiryRequest);

        return new ModelAndView("redirect:/products/" + productId + "/inquiries");
    }

    // REVIEW 상품 문의 1: 상품 상세 페이지에서 상품 문의 조회 페이지로 들어가는 링크
    /**
     * 상품 상세페이지에서 전체 상품 문의 글을 조회 하기 위한 GetMapping 을 지원합니다.
     *
     * @param productId - 상품 문의 조회를 하기 위한 상품 id 입니다.
     * @return 상품 문의 조회 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @GetMapping("/{productId}/inquiries")
    public ModelAndView retrieveProductInquiryByProductId(@PathVariable final Long productId) {
        List<ProductInquiryResponse> productInquiryResponses = productInquiryService.retrieveProductInquiryByProductId(productId);

        ModelAndView mav = new ModelAndView("products/product-inquiry");
        mav.addObject("productInquiryResponses", productInquiryResponses);

        return mav;
    }

    /**
     * 상품 상세 페이지에서 특정 상품 문의를 삭제하기 위한 PostingMapping 을 지원합니다.
     *
     * @param productId - 상품 id 입니다.
     * @param inquiryId - 삭제할 상품 문의 글의 id 입니다.
     * @return 상품 문의 조회 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @PostMapping("/{productId}/inquiries/delete/{inquiryId}")
    public ModelAndView deleteProductInquiry(@PathVariable final Long productId,
                                             @PathVariable final Long inquiryId) {
        productInquiryService.deleteProductInquiry(productId, inquiryId);

        return new ModelAndView("redirect:/products/" + productId + "/inquiries/" + inquiryId);
    }

}
