package com.nhnacademy.marketgg.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.client.dto.request.GivenCouponCreateRequest;
import com.nhnacademy.marketgg.client.service.GivenCouponService;
import com.nhnacademy.marketgg.client.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.client.dto.request.EmailRequest;
import com.nhnacademy.marketgg.client.dto.response.EmailUseResponse;
import com.nhnacademy.marketgg.client.service.MemberService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest({
        MemberController.class,
        MemberAjaxController.class
})
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    GivenCouponService givenCouponService;

    @Test
    @DisplayName("GG 패스 메인 페이지")
    void testIndex() throws Exception {
        when(memberService.retrievePassUpdatedAt(anyLong())).thenReturn(LocalDateTime.now());
        mockMvc.perform(get("/members/{memberId}/ggpass", 1L))
               .andExpect(status().isOk())
               .andExpect(view().name("/ggpass/index"));
    }

    @Test
    @DisplayName("GG 패스 구독 성공")
    void testSubscribePassSuccess() throws Exception {
        doNothing().when(memberService).subscribePass(anyLong());
        when(memberService.retrievePassUpdatedAt(anyLong())).thenReturn(LocalDateTime.now());

        mockMvc.perform(post("/members/{memberId}/ggpass/subscribe", 1L))
               .andExpect(status().is3xxRedirection());

        verify(memberService, times(1)).subscribePass(anyLong());
    }

    @Test
    @DisplayName("GG 패스 구독 실패")
    void testSubscribePassFail() throws Exception {
        doNothing().when(memberService).subscribePass(anyLong());
        when(memberService.retrievePassUpdatedAt(anyLong())).thenReturn(
                LocalDateTime.of(2030, 1, 1, 1, 1, 1));

        mockMvc.perform(post("/members/{memberId}/ggpass/subscribe", 1L))
               .andExpect(view().name("message"));

        verify(memberService, times(0)).subscribePass(anyLong());
    }

    @Test
    @DisplayName("GG 패스 구독해지")
    void testWithdrawPass() throws Exception {
        doNothing().when(memberService).withdrawPass(anyLong());

        mockMvc.perform(post("/members/{memberId}/ggpass/withdraw", 1L))
               .andExpect(status().is3xxRedirection());

        verify(memberService, times(1)).withdrawPass(anyLong());
    }

    @Test
    @DisplayName("사용할 수 있는 이메일 검증 테스트")
    void testUseEmail() throws Exception {

        given(memberService.useEmail(any()))
                .willReturn(new EmailUseResponse(false));

        ObjectMapper objectMapper = new ObjectMapper();

        boolean hasNotReferrer = false;

        mockMvc.perform(post("/marketgg/members/use/email")
                       .content(objectMapper.writeValueAsString(new EmailRequest("aaa@naver.com", hasNotReferrer)))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("회원의 쿠폰 등록")
    void testRegisterCoupon() throws Exception {
        GivenCouponCreateRequest givenCouponRequest = new GivenCouponCreateRequest("couponName");

        String content = objectMapper.writeValueAsString(givenCouponRequest);

        willDoNothing().given(givenCouponService).registerCoupon(anyLong(), any(GivenCouponCreateRequest.class));

        mockMvc.perform(post("/members/{memberId}/coupons", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
               .andExpect(status().is3xxRedirection());

        then(givenCouponService).should().registerCoupon(anyLong(), any(GivenCouponCreateRequest.class));
    }

    @Test
    @DisplayName("회원 보유 쿠폰 목록 조회")
    void testRetrieveOwnCoupons() throws Exception {
        given(givenCouponService.retrieveOwnGivenCoupons(anyLong())).willReturn(List.of());

        MvcResult mvcResult = mockMvc.perform(get("/members/{memberId}/coupons", 1L))
                                     .andExpect(status().isOk())
                                     .andExpect(view().name("/mygg/coupons/index"))
                                     .andReturn();
        Map<String, Object> resultModel = Objects.requireNonNull(mvcResult.getModelAndView()).getModel();

        then(givenCouponService).should().retrieveOwnGivenCoupons(anyLong());
        assertThat(resultModel.get("coupons")).isInstanceOf(List.class);
        assertThat(resultModel.get("memberId")).isInstanceOf(Long.class);
    }

}
