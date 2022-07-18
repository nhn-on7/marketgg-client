package com.nhnacademy.marketgg.client.web;

import com.nhnacademy.marketgg.client.domain.dto.request.ProductCreateRequest;
import com.nhnacademy.marketgg.client.domain.dto.request.ProductModifyRequest;
import com.nhnacademy.marketgg.client.domain.dto.response.ProductResponse;
import com.nhnacademy.marketgg.client.service.ProductInquiryService;
import com.nhnacademy.marketgg.client.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * 상품 컨트롤러 입니다.
 *
 * @version 1.0.0
 */
@Controller
@RequestMapping("/shop/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final ProductInquiryService productInquiryService;
    private static final String DEFAULT_ADMIN_PRODUCT_URI = "/admin/v1/products";

    /**
     * 상품 생성을 위한 PostMapping 을 지원 합니다.
     *
     * @param image          - 사진은 MultipartFile 타입입니다.
     * @param productRequest - 상품 생성을 위한 DTO 입니다.
     * @return - 기본 뷰 페이지를 리턴합니다.
     * @throws IOException
     * @since 1.0.0
     */
    @PostMapping("/create")
    public ModelAndView createProduct(@RequestPart(value = "image") final MultipartFile image,
                                      @ModelAttribute final ProductCreateRequest productRequest)
            throws IOException {

        productService.createProduct(image, productRequest);

        return new ModelAndView("redirect:" + DEFAULT_ADMIN_PRODUCT_URI + "/index");
    }

    /**
     * 상품 생성 페이지 조회를 위한 GetMapping 을 지원 합니다.
     *
     * @return - 상품 생성 폼을 리턴합니다.
     * @since 1.0.0
     */
    @GetMapping("/create")
    public ModelAndView createProduct() {
        return new ModelAndView("products/product-form");
    }

    /**
     * 상품 수정 페이지로 가기 위한 GetMapping 을 지원합니다.
     * 상품의 원래 속성이 기본으로 지정되어 있습니다.
     *
     * @param productId - 상품 PK 입니다.
     * @return - 상품 수정 페이지를 리턴합니다.
     * @since 1.0.0
     */
    @GetMapping("/update/{productId}")
    public ModelAndView updateProduct(@PathVariable final Long productId) {

        ModelAndView mav = new ModelAndView("products/product-modify-form");

        ProductResponse product = productService.retrieveProductDetails(productId);
        mav.addObject("product", product);
        return mav;
    }

    /**
     * 상품 수정을 위한 PostMapping 을 지원 합니다.
     * multipartFile 의 경우 html form 에서 PUT 맵핑을 적용 시키기 어려워서 일단 POST로 구현했습니다.
     * 차후에 PutMapping으로 수정해야 합니다.
     *
     * @param image          - MultipartFile 타입입니다.
     * @param productRequest - 상품 수정을 위한 DTO 입니다.
     * @param productId      - 상품의 PK 입니다.
     * @return - index 페이지를 리턴합니다.
     * @throws IOException
     * @since 1.0.0
     */
    @PostMapping("/update/{productId}")
    public ModelAndView updateProduct(@PathVariable final Long productId,
                                      @RequestPart(value = "image") final MultipartFile image,
                                      @ModelAttribute final ProductModifyRequest productRequest)
            throws IOException {

        productService.updateProduct(productId, image, productRequest);

        return new ModelAndView("redirect:" + DEFAULT_ADMIN_PRODUCT_URI + "/index");
    }

    /**
     * 상품 삭제를 위한 PostMapping을 지원합니다.
     * 소프트 삭제입니다.
     *
     * @param productId - 상품의 PK 입니다.
     * @return - index 페이지를 리턴합니다.
     */
    @PostMapping("/{productId}/delete")
    public ModelAndView deleteProduct(@PathVariable final Long productId) {
        productService.deleteProduct(productId);

        return new ModelAndView("redirect:" + DEFAULT_ADMIN_PRODUCT_URI + "/index");
    }

    /**
     * 관리자가 상품 문의글에 답글을 등록하기 위한 PostMapping 을 지원합니다.
     *
     * @param productId -
     * @return - 답글 등록 후 상품 문의 조회 페이지로 리턴합니다.
     */
    @PostMapping("/{productId}/inquiries")
    public ModelAndView updateProductInquiryReply(@PathVariable final Long productId) {
        productInquiryService.updateProductInquiryReply(productId);

        return new ModelAndView("redirect:/products/" + productId +  "/inquiries");
    }

}
