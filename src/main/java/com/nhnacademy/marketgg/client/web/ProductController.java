package com.nhnacademy.marketgg.client.web;

import com.nhnacademy.marketgg.client.domain.dto.request.ProductCreateRequest;
import com.nhnacademy.marketgg.client.domain.dto.response.ProductResponse;
import com.nhnacademy.marketgg.client.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final static String DEFAULT_PRODUCT_URI = "/admin/v1/products";

    @PostMapping
    ModelAndView createProduct(@ModelAttribute final ProductCreateRequest productRequest) {

        ModelAndView mav = new ModelAndView("redirect:" + DEFAULT_PRODUCT_URI + "/index");
        productService.createProduct(productRequest);

        return mav;
    }

    @GetMapping
    ModelAndView retrieveProducts() {
        List<ProductResponse> products = productService.retrieveProducts();

        ModelAndView mav = new ModelAndView(DEFAULT_PRODUCT_URI + "/index");
        mav.addObject("Products", products);

        return mav;
    }
}
